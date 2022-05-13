package com.zs.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zs.config.Const2;
import com.zs.config.ConstRedisKeyPrefix;
import com.zs.dto.ThumbsDTO;
import com.zs.handler.ThumbsRedisHelper;
import com.zs.handler.UniversalException;
import com.zs.mapper.LikeMapper;
import com.zs.pojo.Like;
import com.zs.service.ThumbsService;
import com.zs.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Created by zs on 2022/5/12.
 */
@Service
public class ThumbsServiceImpl implements ThumbsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThumbsServiceImpl.class);

    @Resource
    private ThumbsRedisHelper thumbsRedisHelper;

    @Resource
    private LikeMapper likeMapper;

    /**
     * 点赞
     * @param thumbsDTO 点赞信息
     * @return code = 200 成功
     *         code = 525 失败
     */
    @Override
    public ResultVO like(ThumbsDTO thumbsDTO) throws JsonProcessingException {
        Boolean likeResult = thumbsRedisHelper.like(ConstRedisKeyPrefix.THUMBS_PREFIX + thumbsDTO.getMid(), thumbsDTO);
        if (likeResult) {
            return new ResultVO(Const2.SERVICE_SUCCESS, "success", null);
        }
        return new ResultVO(Const2.SERVICE_FAIL, "fail", null);
    }

    /**
     * 获取用户所有点赞记录
     * @param mid 用户id
     * @return
     */
    @Override
    public ResultVO getLikesByMid(Long mid) throws JsonProcessingException {
        List<Like> userLikes = thumbsRedisHelper.getUserLikes(ConstRedisKeyPrefix.USER_ALL_THUMBS + mid);
        if (userLikes == null) {
            // 需查询数据可库，查询完毕后缓存
            userLikes = likeMapper.listLikesByMid(mid);
            thumbsRedisHelper.cacheUserLikes(ConstRedisKeyPrefix.USER_ALL_THUMBS + mid, userLikes);
        }
        return new ResultVO(Const2.SERVICE_SUCCESS, "success", userLikes);
    }

    // TODO 设计判断用户是否点赞当前文章接口
}
