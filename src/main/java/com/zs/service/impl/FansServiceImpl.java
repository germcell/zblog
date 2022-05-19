package com.zs.service.impl;

import com.zs.config.Const2;
import com.zs.config.ConstRedisKeyPrefix;
import com.zs.handler.FansRedisHelper;
import com.zs.mapper.FansMapper;
import com.zs.pojo.Fans;
import com.zs.service.FansService;
import com.zs.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @Created by zs on 2022/4/30.
 */
@Service
public class FansServiceImpl implements FansService {

    private Logger logger = LoggerFactory.getLogger(FansServiceImpl.class);

    @Resource
    private FansMapper fansMapper;

    @Resource
    private FansRedisHelper fansRedisHelper;

    /**
     * 关注用户（redis）
     *      先判断请求参数是否为空，然后判断该请求用户是否已经关注过此用户(一般前端会根据是否关注发送对应的请求，
     *      但有可能前端样式渲染有误造成这种情况),如果确认未关注后，则执行关注操作
     * @param fans 【uid被关注人】 【uid2主关注人】
     * @return code == 200 成功
     *         code == 701 已经关注过该用户
     * TODO 定时同步关注记录到数据库
     */
    @Transactional
    @Override
    public ResultVO becomeFans(Fans fans) {
        String key = ConstRedisKeyPrefix.WRITER_FANS_TO_REDIS + fans.getUid2();
        String value = String.valueOf(fans.getUid());
        // 1.判断是否已经关注
        boolean isFollow = fansRedisHelper.isFollow(key, value);
        if (isFollow) {
            return new ResultVO(Const2.ALREADY_FOLLOW, "has followed this user", fans);
        }
        // 2.将关注信息添加到redis
        fansRedisHelper.follow(key, value);
        return new ResultVO(Const2.SERVICE_SUCCESS, "success", fans);
    }

    /**
     * 查询用户的关注列表
     *    先查询redis，没有数据则查询DB
     * @param uid 用户id
     * @return code == 200 成功
     */
    @Override
    public ResultVO listFansByUid(String uid) {
        Set<String> fansToRedis = fansRedisHelper.getAllFans(ConstRedisKeyPrefix.WRITER_FANS_TO_REDIS + uid);
        if (fansToRedis == null) {
            Long writerId = null;
            writerId = Long.parseLong(uid);
            Fans condition = new Fans();
            condition.setUid2(writerId);
            List<Fans> fansToDB = fansMapper.listByCondition(condition);
            // 将用户关注列表缓存到redis
            if (fansToDB.size() > 0) {
                fansRedisHelper.cacheAllFansData(ConstRedisKeyPrefix.WRITER_FANS_TO_REDIS + uid, fansToDB);
            }
            fansToRedis = fansRedisHelper.getAllFans(ConstRedisKeyPrefix.WRITER_FANS_TO_REDIS + uid);
            return new ResultVO(Const2.SERVICE_SUCCESS, "success", fansToRedis);
        }
        return new ResultVO(Const2.SERVICE_SUCCESS, "success", fansToRedis);
    }

    /**
     * 取消关注
     *   先判断是否已经关注，是则取关
     * @param fans
     * @return code == 200 取关成功
     *         code == 702 取关失败，还未关注过
     */
    @Override
    public ResultVO unFollow(Fans fans) {
        String key = ConstRedisKeyPrefix.WRITER_FANS_TO_REDIS + fans.getUid2();
        String value = String.valueOf(fans.getUid());
        Boolean isFollow = fansRedisHelper.isFollow(key, value);
        if (isFollow) {
            fansRedisHelper.unfollow(key, value);
            return new ResultVO(Const2.SERVICE_SUCCESS, "success", fans);
        }
        return new ResultVO(Const2.NO_FOLLOW, "not followed yet", fans);
    }

    /**
     * 同步redis中的关注信息到DB
     *   因为用户关注记录是存储在redis中的，可选择将记录同步到数据库。
     *   redis储存的以【allWriterFans_ + 用户id】为key，被关注用户id为value的set集合。
     *   同步规则：
     *      1.获取所有满足allWriterFans_为前缀的key；
     *      2.遍历每一个key，获取key对应的value；(Set<String>) 【主，因为redis中的数据才是最新的关注信息，可能会在原有基础上取关，所以要以redis为主，DB为辅】
     *      3.判断value是否不为null，或长度不为0；
     *      4.根据key截取得到用户id，从数据库中查询用户所有的关注记录；(List<Fans>)
     *      5.使用Redis中的数据和DB中的数据进行匹配，得到 addSet<String>；
     *      6.使用DB中的数据和Redis中的数据进行匹配，得到 delList<Fans>；
     *      7.执行删除操作
     *      8.执行添加操作
     *      9.返回结果
     *
     *      步骤5->逻辑
     *      数据库查询结果：List [{id:1, uid:1, uid2:7},{id:2, uid:3, uid2:7},{id:3, uid:5, uid2:7}]  用户7关注了用户1,3,5 DB
     *      Redis查询结果：Set  [1,2,3,4] 用户7关注了1,2,3,4 Redis
     *
     *      delList【匹配后需要删除的记录】 5
     *      addList【匹配后需要新增的记录】 2 4
     *
     *      set 1 3
     *      list 1 3
     *      【set发起匹配，在DB中匹配到，不操作】
     *
     *      set 2
     *      list 1 3
     *      【set发起匹配，在DB中未匹配到，需新增 addSet.add()】
     *
     *      set 4
     *      list 1 3 5
     *      【set发起匹配，在DB中未匹配到，需新增 addSet.add()】
     *
     *      list 1 3 5
     *      set 1 2 3 4
     *      【list发起匹配，匹配不成功的需删除 delList.add()】
     *
     * @return
     */
    @Override
    public ResultVO syncFans() {
        AtomicInteger row = new AtomicInteger(0);
        FansUtils fansUtils = new FansUtils();
        Fans condition = new Fans();

        /**
         * TODO 考虑到处理用户量多的情况，选择创建一个线程池来执行同步操作
         */
//        int systemThreads = Runtime.getRuntime().availableProcessors();
//        ThreadPoolExecutor executor = new ThreadPoolExecutor((systemThreads / 2),
//                                                              systemThreads,
//                                                              5,
//                                                              TimeUnit.SECONDS,
//                                                              new ArrayBlockingQueue<>(10),
//                                                              Executors.defaultThreadFactory(),
//                                                              new ThreadPoolExecutor.DiscardOldestPolicy());

        // step1
        Set<String> keys = fansRedisHelper.getPatternKey(ConstRedisKeyPrefix.WRITER_FANS_TO_REDIS + "*");
        if (Objects.isNull(keys)) {
            return new ResultVO(Const2.SYNC_SUCCESS_NO_OPERATION, "success,no operation", null);
        }
        // step2
        keys.stream().forEach((key) -> {
            // step3 value是用户在redis中关注记录的集合
            Set<String> value = fansRedisHelper.getAllFans(key);
            if (!Objects.isNull(value)) {
                // step4 fansByUid2是用户在数据库中的关注记录的集合
                long uid2 = Long.parseLong(key.split("_")[1]);
                condition.setUid2(uid2);
                List<Fans> fansByUid2 = fansMapper.listByCondition(condition);

                // step5 得到需要新增的记录集合
                Set<String> addSet = value.stream()
                        .filter((v) -> !fansUtils.isContains(fansByUid2, v))
                        .collect(Collectors.toSet());

                // step6 得到需要删除的记录集合
                List<Fans> delList = fansByUid2.stream().filter((f) -> {
                    if (!value.contains(String.valueOf(f.getUid()))) {
                        return true;
                    }
                    return false;
                }).collect(Collectors.toList());

                // step7
                delList.forEach((df) -> {
                    try {
                        fansMapper.deleteByCondition(df);
                    } catch (Exception e) {
                        logger.info("同步Fans数据表记录异常【删除】{}", e);
                    }
                });

                // step8
                Fans save = new Fans();
                addSet.forEach((as) -> {
                    long uid = Long.parseLong(as);
                    save.setUid(uid);
                    save.setUid2(uid2);
                    save.setJoinTime(new Date());
                    fansMapper.insert(save);
                });
                row.incrementAndGet();
            }
        });

        // step9
        return new ResultVO(Const2.SYNC_SUCCESS, "sync tb_fans success", row);
    }

    /**
     * 获取请求用户对访问用户的关注状态
     * @param fans
     * @return
     */
    @Override
    public ResultVO getFollowStatus(Fans fans) {
        String key = ConstRedisKeyPrefix.WRITER_FANS_TO_REDIS + fans.getUid2();
        String value = String.valueOf(fans.getUid());
        Boolean follow = fansRedisHelper.isFollow(key, value);
        return new ResultVO(Const2.SERVICE_SUCCESS, "success", follow);
    }

    class FansUtils {
        /**
         * DB是否包含Redis的记录
         * @param fans
         * @param uid
         * @return
         */
        public Boolean isContains(List<Fans> fans, String uid) {
            return fans.stream().anyMatch((f) -> Objects.equals(uid, String.valueOf(f.getUid())));
        }
    }
}


