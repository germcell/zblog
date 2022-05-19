package com.zs.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zs.config.Const2;
import com.zs.config.ConstRedisKeyPrefix;
import com.zs.handler.CategoryRedisHelper;
import com.zs.handler.UniversalException;
import com.zs.mapper.CopyrightMapper;
import com.zs.pojo.Copyright;
import com.zs.service.CopyrightService;
import com.zs.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @Created by zs on 2022/3/4.
 */
@Service
public class CopyrightServiceImpl implements CopyrightService {

    @Autowired
    private CopyrightMapper copyrightMapper;

    @Resource
    private CategoryRedisHelper categoryRedisHelper;

    @Override
    public List<Copyright> listCopyright() {
        return copyrightMapper.listCopyright();
    }

    /**
     * 获取所有文章版权信息(2.0)
     * @param status
     * @return
     */
    @Override
    public ResultVO listCopyright(Integer status) {
        try {
            List copyrights = categoryRedisHelper.getAll(ConstRedisKeyPrefix.ALL_COPYRIGHTS);
            if (Objects.isNull(copyrights)) {
                copyrights = copyrightMapper.listCopyright();
                categoryRedisHelper.cache(ConstRedisKeyPrefix.ALL_COPYRIGHTS, copyrights);
                return new ResultVO(Const2.SERVICE_SUCCESS, "success", copyrights);
            }
            return new ResultVO(Const2.SERVICE_SUCCESS, "success", copyrights);
        } catch (JsonProcessingException e) {
            throw new UniversalException("获取文章版权信息异常", e);
        }
    }
}
