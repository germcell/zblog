package com.zs.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zs.config.Const;
import com.zs.config.Const2;
import com.zs.config.ConstRedisKeyPrefix;
import com.zs.handler.ArticleRedisHelper;
import com.zs.handler.RandomUtils;
import com.zs.mapper.AdMapper;
import com.zs.mapper.BlogOutlineMapper;
import com.zs.mapper.WriterMapper;
import com.zs.pojo.Ad;
import com.zs.pojo.BlogOutline;
import com.zs.pojo.Writer;
import com.zs.service.BlogOutlineService;
import com.zs.service.IndexService;
import com.zs.vo.BlogOutlineVO;
import com.zs.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @Created by zs on 2022/4/20.
 */
@Service
public class IndexServiceImpl implements IndexService {

    @Resource
    private AdMapper adMapper;
    @Resource
    private BlogOutlineMapper blogOutlineMapper;
    @Resource
    private WriterMapper writerMapper;
    @Resource
    private ArticleRedisHelper articleRedisHelper;

    private Logger logger = LoggerFactory.getLogger(IndexServiceImpl.class);

    /**
     * 查询首页展示信息
     * @return
     */
    @Override
    public ResultVO getIndexData() {
        try {
            HashMap<String, Object> indexPageData = articleRedisHelper.getIndexPageData(ConstRedisKeyPrefix.INDEX_PAGE_DATA);
            if (indexPageData != null) {
                // TODO 因为设置了过期时间，所以要防止缓存穿透
                return new ResultVO(Const2.SERVICE_SUCCESS, "success", indexPageData);
            }
            HashMap<String,Object> map = new HashMap<>();
            // 1.查询第一页的文章概要信息，已按发布时间降序排列
            PageHelper.startPage(1, Const.BLOG_PAGE_ROWS);
            List<BlogOutlineVO> blogOutlineVOS = blogOutlineMapper.listBlogOutlines();
            PageInfo<BlogOutlineVO> blogOutlineVOPageInfo = new PageInfo<>(blogOutlineVOS);
            map.put("blogPageInfo", blogOutlineVOPageInfo);
            // 2.查询首页活动
            Ad ad = adMapper.getAdByStatus(1);
            if (ad.getType() == 2) {
                BlogOutline blogOutlineById = blogOutlineMapper.getBlogOutlineById((Long) ad.getBid());
                if (blogOutlineById == null) {
                    logger.info("当前首推文章已被删除,将随机选择一篇文章...,请管理员重新选择");
                    Integer randomIndex = RandomUtils.generateNum(blogOutlineVOS.size());
                    map.put("ad", blogOutlineVOS.get(randomIndex));
                } else {
                    map.put("ad", blogOutlineById);
                }
            } else {
                map.put("ad", ad);
            }
            // 3.查询5名作者信息，策略：已文章发表数量及作者状态为1（正常）
            Writer condition = new Writer();
            condition.setWriterStatus(1);
            condition.setArticleNum(5);
            PageHelper.startPage(1, 7);
            List<Writer> writers = writerMapper.listWriterByCondition(condition);
            writers.stream().forEach(w -> {w.setPwd(null);});
            PageInfo<Writer> writerPageInfo = new PageInfo<>(writers);
            map.put("writers", writerPageInfo.getList());
            // 4.查询浏览量前15的文章概要信息(排行榜)
            // TODO redis实现
            PageHelper.startPage(1, 15);
            List<BlogOutline> viewSort = blogOutlineMapper.listSortByViewsBlogOutline();
            PageInfo<BlogOutline> viewSortPageInfo = new PageInfo<>(viewSort);
            map.put("hotArticle", viewSortPageInfo);
            // 5.封装返回数据
            articleRedisHelper.cacheIndexPageData(ConstRedisKeyPrefix.INDEX_PAGE_DATA, map);
            ResultVO resultVO = new ResultVO(Const2.SERVICE_SUCCESS, "success", map);
            return resultVO;
        } catch (Exception e) {
            logger.info("获取首页数据失败{}", e);
            ResultVO resultVO = new ResultVO(Const2.SERVICE_FAIL, "fail", null);
            return resultVO;
        }
    }
}
