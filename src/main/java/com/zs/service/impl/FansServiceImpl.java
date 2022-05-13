package com.zs.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zs.config.Const2;
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
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
     * @param fans 【uid关注人】 【uid2被关注人】
     * @return
     */
    @Transactional
    @Override
    public ResultVO becomeFans(Fans fans) {
        if (fans.getUid() == null || fans.getUid2() == null) {
            return new ResultVO(Const2.PARAMETERS_IS_NULL, "request parameters is null", null);
        }
        try {
            // 1.判断是否已经关注
            List<Fans> fansToRedis = fansRedisHelper.allFansFromDBToList("DB-" + fans.getUid2());
            if (fansToRedis != null) {
                Iterator<Fans> it = fansToRedis.iterator();
                while (it.hasNext()) {
                    Fans next = it.next();
                    if (Objects.equals(fans.getUid2(), next.getUid2())) {
                        return new ResultVO(Const2.ALREADY_FOLLOW, "has followed this user ", null);
                    }
                }
            }
            // 2.添加到redis
            Long result = fansRedisHelper.becomeFans( "F-" + fans.getUid(), String.valueOf(fans.getUid2()));
            if (result == 1) {
                return new ResultVO(Const2.SERVICE_SUCCESS, "success", null);
            }
            return new ResultVO(Const2.SERVICE_FAIL, "fail", null);
        } catch (JsonProcessingException e) {
            return new ResultVO(Const2.SERVICE_FAIL, "fail", null);
        }
        // TODO 用户粉丝增加，后续使用定时任务定时做更新
        // fans.setJoinTime(new Date());
        // fansMapper.insert(fans);
    }

    /**
     * 查询当前用户已关注的作者
     * @param uid
     * @return
     */
    @Override
    public ResultVO listFansByUid(String uid) {
        List<Fans> fansToDB = null;
        try {
            List<Fans> fansToRedis = fansRedisHelper.allFansFromDBToList("DB-" + uid);
            if (fansToRedis == null) {
                Long writerId = null;
                writerId = Long.parseLong(uid);
                Fans condition = new Fans();
                condition.setUid(writerId);
                fansToDB =  fansMapper.listByCondition(condition);
                // 缓存到redis
                fansRedisHelper.cacheRedis("DB-" + uid, fansToDB);
                return new ResultVO(Const2.SERVICE_SUCCESS, "success", fansToDB);
            }
            return new ResultVO(Const2.SERVICE_SUCCESS, "success", fansToRedis);
        } catch (JsonProcessingException e) {
            logger.info("查询关注用户信息异常(redis) {}",e.getMessage());
            return new ResultVO(Const2.SERVICE_SUCCESS, "success", fansToDB);
        } catch (ClassCastException e) {
            logger.info("查询关注用户信息异常(not found) {}",e.getMessage());
            return new ResultVO(Const2.NOT_FOUND, "writer not found", null);
        }
    }

    /**
     * 取关
     * @param fans
     * @return
     */
    @Override
    public ResultVO unFollow(Fans fans) {
        if (fans.getUid() == null || fans.getUid2() == null) {
            return new ResultVO(Const2.PARAMETERS_IS_NULL, "request parameters is null", null);
        }
        try {
            int row = fansMapper.deleteByCondition(fans);
            if (row == 1) {
                Fans condition = new Fans();
                condition.setUid(fans.getUid());
                // 更新缓存
                fansRedisHelper.removeKey("DB-" + fans.getUid());
                fansRedisHelper.cacheRedis("DB-" + fans.getUid(), fansMapper.listByCondition(condition));
                return new ResultVO(Const2.SERVICE_SUCCESS, "unfollow success", null);
            }
            return new ResultVO(Const2.SERVICE_FAIL, "fail", null);
        } catch (Exception e) {
            return new ResultVO(Const2.SERVICE_FAIL, "fail", null);
        }
    }

    /**
     * 同步redis中的关注信息到DB
     * @return
     */
    @Override
    public ResultVO syncFans() throws JsonProcessingException {
        // keys集合储存所有的键，每个用户的关注记录
        Set<String> keys = fansRedisHelper.allFansOfKey("F-*");
        if (keys.size() == 0) {
            return new ResultVO(Const2.REGISTER_SUCCESS, "success", keys.size());
        }
        Fans f = new Fans();
        Set<String> values = null;
        Iterator<String> keysIt = keys.iterator();
        Iterator<String> valuesIt = null;
        // 1.遍历查询到的所有key
        while (keysIt.hasNext()) {
            String k = keysIt.next(); // F-2
            // 2.获取key对应的value
            values = fansRedisHelper.allFans(k);
            Long k_l = null;
            if (values.size() > 0) {
                // 4.格式化key 【"F-2" -> 2】，作为uid
                k_l = Long.parseLong(k.split("-")[1]);
                // 5.遍历对应的value
                valuesIt = values.iterator();
                while (valuesIt.hasNext()) {
                    String v = valuesIt.next();
                    // 6.格式化value 【"1" -> 1】，作为uid2
                    Long v_l = Long.parseLong(v);
                    // 7.插入记录
                    f.setUid(k_l);
                    f.setUid2(v_l);
                    f.setJoinTime(new Date());
                    fansMapper.insert(f);
                    // 8.清除DB-2
                    fansRedisHelper.removeKey("DB-" + k_l);
                    // 9.更新DB-2到redis
                    Fans condition = new Fans();
                    condition.setUid(k_l);
                    fansRedisHelper.cacheRedis("DB-" + k_l, fansMapper.listByCondition(condition));
                    // 10.插入数据库后，清除key
                    fansRedisHelper.removeKey(k);
                }
            }
        }
        return new ResultVO(Const2.REGISTER_SUCCESS, "success", keys.size());
    }

}
