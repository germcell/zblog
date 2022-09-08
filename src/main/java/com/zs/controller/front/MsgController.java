package com.zs.controller.front;

import com.alibaba.fastjson.JSON;
import com.zs.dto.MsgDTO;
import com.zs.service.impl.MsgService;
import com.zs.vo.ResultVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author zengshuai
 * @create 2022-09-07 15:15
 */
@RestController
@CrossOrigin
@RequestMapping("/v2/msg")
public class MsgController {

    @Resource
    private MsgService msgService;

    @PostMapping("/privateMsg")
    public ResultVO privateMsg(@RequestBody MsgDTO msgDTO) {
        msgService.productMsg(JSON.toJSONString(msgDTO));
        return new ResultVO(100000,"成功", null);
    }

}
