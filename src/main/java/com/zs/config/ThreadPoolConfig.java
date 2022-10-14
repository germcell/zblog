package com.zs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zengshuai
 * @create 2022-10-12 22:26
 */
@Configuration
public class ThreadPoolConfig {

    /**
     * 发送邮件线程池 sendMailThreadPool
     * @return
     */
    @Bean("sendMailThreadPool")
    public ThreadPoolExecutor getSendMailThreadPool() {
        return new ThreadPoolExecutor(2, 5, 30, TimeUnit.SECONDS,
                                      new LinkedBlockingDeque<>(5),
                                      new MyThreadFactory("SendMailThreadPool", new AtomicInteger(1)),
                                      new ThreadPoolExecutor.CallerRunsPolicy());
    }

    /**
     * ES数据处理线程池 handleEsDataThreadPool（包括导入数据到ES）
     * @return
     */
    @Bean("handleEsDataThreadPool")
    public ThreadPoolExecutor getHandleEsDataThreadPool() {
        return new ThreadPoolExecutor(2, 5, 30, TimeUnit.SECONDS,
                                      new LinkedBlockingDeque<>(5),
                                      new MyThreadFactory("HandleEsDataThreadPool", new AtomicInteger(1)),
                                      new ThreadPoolExecutor.CallerRunsPolicy());
    }

}

class MyThreadFactory implements ThreadFactory {

    private final String threadPoolName;
    private final AtomicInteger threadNum;

    public MyThreadFactory(String threadPoolName, AtomicInteger threadNum) {
        this.threadPoolName = threadPoolName;
        this.threadNum = threadNum;
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, threadPoolName + "-" + threadNum.getAndIncrement());
    }
}