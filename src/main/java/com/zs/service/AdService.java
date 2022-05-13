package com.zs.service;

import com.zs.pojo.Ad;

/**
 * @Created by zs on 2022/4/20.
 */
public interface AdService {
    /**
     * 获取当前活动、推荐文章
     * @return
     */
    Ad getAd();
}
