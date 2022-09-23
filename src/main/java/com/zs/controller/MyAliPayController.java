package com.zs.controller;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.zs.config.Const2;
import com.zs.dto.AliPayDTO;
import com.zs.handler.DateUtils;
import com.zs.service.OrderService;
import com.zs.vo.ResultVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Air
 * @create 2022-06-04 16:11
 */
@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/v2/my-alipay")
public class MyAliPayController {

    @Resource
    private OrderService orderService;

    /**
     * 申请支付接口
     * @param aliPayDTO
     * @return code == 301 支付参数未按规定提供
     *         code == 525 失败【支付金额为0】、【传递非数字的金额，格式化异常】，【调用支付宝支付异常】
     *         code == 200 成功
     */
    @ApiIgnore
    @GetMapping("/pay")
    public String pay(AliPayDTO aliPayDTO) {
        // 0.参数校验
        if (Objects.isNull(aliPayDTO.getPaySubject()) || Objects.isNull(aliPayDTO.getPayTotalAmount()) ||
            Objects.isNull(aliPayDTO.getUid()) || Objects.isNull(aliPayDTO.getUid2())) {
            return "pay fail";
        }
        try {
            double v = Double.parseDouble(aliPayDTO.getPayTotalAmount());
            if (v == 0) {
                return "pay fail";
            }
        } catch (NumberFormatException e) {
            log.error("支付失败，支付金额有误: {}",e);
            return "pay fail";
        }

        // 1.生成订单
        aliPayDTO.setPayOrderNo(DateUtils.dateToString(new Date(), "yyyyMMddHHmmss") + System.currentTimeMillis());
        orderService.createAppreciateOrder(aliPayDTO);

        // 2.开始支付
        AlipayTradePagePayResponse alipayResponse;
        try {
            log.info("用户申请支付");
            alipayResponse = Factory.Payment.Page()
                    .pay(aliPayDTO.getPaySubject(), aliPayDTO.getPayOrderNo(), aliPayDTO.getPayTotalAmount(), "");
        } catch (Exception e) {
            log.error("支付异常: {}", e);
            return "pay fail";
        }
        return alipayResponse.getBody();
    }

    /**
     * 支付回调接口
     * @param request
     * @return
     * @throws Exception
     */
    @ApiIgnore
    @PostMapping("/pay-notify")
    public String notify(HttpServletRequest request) throws Exception {
        if (Objects.equals("TRADE_SUCCESS", request.getParameter("trade_status"))) {
            log.info("用户支付成功，进入回调接口");

            final int ORDER_PAY_SUCCESS = 1;

            // 获取从支付宝接收的参数，并完成支付宝验签
            Map<String, String> checkParameters = new HashMap<>();
            Map<String, String[]> requestParameterMap = request.getParameterMap();

            requestParameterMap.entrySet().stream().forEach(kv -> {
                checkParameters.put(kv.getKey(), kv.getValue()[0]);
            });

            // 商户订单号，支付宝支付流水号，买家支付时间
            String payOrderNo = checkParameters.get("out_trade_no");
            String alipayTradeNo = checkParameters.get("trade_no");
            String gmtPayment = checkParameters.get("gmt_payment");

            if (Factory.Payment.Common().verifyNotify(checkParameters)) {
                log.info("支付宝验签通过=> 订单号: {} 支付宝流水号: {} 支付时间: {}", payOrderNo, alipayTradeNo, gmtPayment);
                orderService.updateOrderStatus(ORDER_PAY_SUCCESS, payOrderNo, alipayTradeNo, gmtPayment);
                log.info("订单状态修改成功:已支付");
            }
        }
        return "success";
    }

}
