//package com.zs;
//
//import com.alibaba.fastjson.JSON;
//import com.aliyuncs.AcsResponse;
//import com.aliyuncs.DefaultAcsClient;
//import com.aliyuncs.IAcsClient;
//import com.aliyuncs.RpcAcsRequest;
//import com.aliyuncs.exceptions.ClientException;
//import com.aliyuncs.exceptions.ServerException;
//import com.aliyuncs.imageaudit.model.v20191230.ScanTextRequest;
//import com.aliyuncs.imageaudit.model.v20191230.ScanTextResponse;
//import com.aliyuncs.profile.DefaultProfile;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author zengshuai
// * @create 2022-10-03 13:53
// */
//@SpringBootTest
//public class AliContentExamineTest {
//
//    private static IAcsClient client = null;
//
//    @Test
//    void contentTest() throws Exception {
//        // LTAI5tQ2jo8qCDwe4Ves5UQm
//        // VSDRJpwOtcFipA04ckRnrKBUHDwTmw
//        DefaultProfile profile = DefaultProfile.getProfile("cn-shanghai",
//                                                           "LTAI5tQ2jo8qCDwe4Ves5UQm",
//                                                            "VSDRJpwOtcFipA04ckRnrKBUHDwTmw");
//
//        client = new DefaultAcsClient(profile);
//
//        testScanText();
//
//    }
//
//    public static void testScanText() throws Exception {
//        System.out.println("--------  文章内容审核 --------------");
//        ScanTextRequest req = new ScanTextRequest();
//        List<ScanTextRequest.Labels> labelss = new ArrayList<>();
//        ScanTextRequest.Labels label1 = new ScanTextRequest.Labels();
//        label1.setLabel("politics");
//        labelss.add(label1);
//        ScanTextRequest.Labels label2 = new ScanTextRequest.Labels();
//        label2.setLabel("abuse");
//        labelss.add(label2);
//        ScanTextRequest.Labels label3 = new ScanTextRequest.Labels();
//        label3.setLabel("terrorism");
//        labelss.add(label3);
//        ScanTextRequest.Labels label4 = new ScanTextRequest.Labels();
//        label4.setLabel("porn");
//        labelss.add(label4);
//        ScanTextRequest.Labels label5 = new ScanTextRequest.Labels();
//        label5.setLabel("ad");
//        labelss.add(label5);
//
//        req.setLabelss(labelss);
//
//        List<ScanTextRequest.Tasks> tasks = new ArrayList<>();
//        ScanTextRequest.Tasks task1 = new ScanTextRequest.Tasks();
//        task1.setContent("本校小额贷款，安全、快捷、方便、无抵押，随机随贷，当天放款，上门服务。联系weixin 123456");
//        tasks.add(task1);
//        req.setTaskss(tasks);
//
//
//        ScanTextResponse response = getAcsResponse(req);
//
//        System.out.println(JSON.toJSONString(response));
//    }
//
//    private static <R extends RpcAcsRequest<T>, T extends AcsResponse> T getAcsResponse(R req) throws Exception {
//        try {
//            return client.getAcsResponse(req);
//        } catch (ServerException e) {
//            // 服务端异常
//            System.out.println(String.format("ServerException: errCode=%s, errMsg=%s", e.getErrCode(), e.getErrMsg()));
//            throw e;
//        } catch (ClientException e) {
//            // 客户端错误
//            System.out.println(String.format("ClientException: errCode=%s, errMsg=%s", e.getErrCode(), e.getErrMsg()));
//            throw e;
//        } catch (Exception e) {
//            System.out.println("Exception:" + e.getMessage());
//            throw e;
//        }
//    }
//}
