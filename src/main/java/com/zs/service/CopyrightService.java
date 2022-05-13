package com.zs.service;

import com.zs.pojo.Copyright;
import com.zs.vo.ResultVO;

import java.util.List;

/**
 * @Created by zs on 2022/3/4.
 */
public interface CopyrightService {

    List<Copyright> listCopyright();

    ResultVO listCopyright(Integer status);

}
