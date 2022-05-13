package com.zs.service.impl;

import com.zs.config.Const2;
import com.zs.mapper.CopyrightMapper;
import com.zs.pojo.Copyright;
import com.zs.service.CopyrightService;
import com.zs.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Created by zs on 2022/3/4.
 */
@Service
public class CopyrightServiceImpl implements CopyrightService {

    @Autowired
    private CopyrightMapper copyrightMapper;

    @Override
    public List<Copyright> listCopyright() {
        return copyrightMapper.listCopyright();
    }

    @Override
    public ResultVO listCopyright(Integer status) {
        return new ResultVO(Const2.SERVICE_SUCCESS, "success", copyrightMapper.listCopyright());
    }
}
