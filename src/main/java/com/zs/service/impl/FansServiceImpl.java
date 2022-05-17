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
/**
 * @Created by zs on 2022/4/30.
 */
@Service
public class FansServiceImpl implements FansService {

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
     * @return
     */
//    @Override
//    public ResultVO syncFans() throws JsonProcessingException {
//        // keys集合储存所有的键，每个用户的关注记录
//        Set<String> keys = fansRedisHelper.allFansOfKey("F-*");
//        if (keys.size() == 0) {
//            return new ResultVO(Const2.REGISTER_SUCCESS, "success", keys.size());
//        }
//        Fans f = new Fans();
//        Set<String> values = null;
//        Iterator<String> keysIt = keys.iterator();
//        Iterator<String> valuesIt = null;
//        // 1.遍历查询到的所有key
//        while (keysIt.hasNext()) {
//            String k = keysIt.next(); // F-2
//            // 2.获取key对应的value
//            values = fansRedisHelper.allFans(k);
//            Long k_l = null;
//            if (values.size() > 0) {
//                // 4.格式化key 【"F-2" -> 2】，作为uid
//                k_l = Long.parseLong(k.split("-")[1]);
//                // 5.遍历对应的value
//                valuesIt = values.iterator();
//                while (valuesIt.hasNext()) {
//                    String v = valuesIt.next();
//                    // 6.格式化value 【"1" -> 1】，作为uid2
//                    Long v_l = Long.parseLong(v);
//                    // 7.插入记录
//                    f.setUid(k_l);
//                    f.setUid2(v_l);
//                    f.setJoinTime(new Date());
//                    fansMapper.insert(f);
//                    // 8.清除DB-2
//                    fansRedisHelper.removeKey("DB-" + k_l);
//                    // 9.更新DB-2到redis
//                    Fans condition = new Fans();
//                    condition.setUid(k_l);
//                    fansRedisHelper.cacheRedis("DB-" + k_l, fansMapper.listByCondition(condition));
//                    // 10.插入数据库后，清除key
//                    fansRedisHelper.removeKey(k);
//                }
//            }
//        }
//        return new ResultVO(Const2.REGISTER_SUCCESS, "success", keys.size());
//    }


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
}
