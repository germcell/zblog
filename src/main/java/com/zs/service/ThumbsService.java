package com.zs.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zs.dto.ThumbsDTO;
import com.zs.vo.ResultVO;

import java.util.Optional;

/**
 * @Created by zs on 2022/5/12.
 */
public interface ThumbsService {
    /**
     * 点赞
     * @param thumbsDTO 点赞信息
     * @return
     */
    ResultVO like(ThumbsDTO thumbsDTO) throws JsonProcessingException;

    /**
     * 获取文章点赞数
     * @param mid 用户id
     * @return
     */
    ResultVO getLikesByBid(Long mid) throws JsonProcessingException;

    /**
     * 判断用户是否已为当前文章点赞
     * @param thumbsDTO 点赞信息
     * @return
     */
    ResultVO isLike(ThumbsDTO thumbsDTO) throws JsonProcessingException;

    /**
     * 取消点赞
     * @param thumbsDTO
     * @return
     */
    ResultVO cancelLike(ThumbsDTO thumbsDTO);

    /**
     * 同步点赞数量
     * @return
     */
    ResultVO syncLike();
}
