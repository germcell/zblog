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
     * 获取用户所有点赞记录
     * @param mid 用户id
     * @return
     */
    ResultVO getLikesByMid(Long mid) throws JsonProcessingException;
}
