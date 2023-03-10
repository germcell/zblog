package com.zs.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zs.config.Const2;
import com.zs.config.ConstRedisKeyPrefix;
import com.zs.dto.ThumbsDTO;
import com.zs.handler.FansRedisHelper;
import com.zs.handler.ThumbsRedisHelper;
import com.zs.mapper.BlogMapper;
import com.zs.mapper.BlogOutlineMapper;
import com.zs.mapper.LikeMapper;
import com.zs.pojo.Blog;
import com.zs.pojo.BlogOutline;
import com.zs.pojo.Like;
import com.zs.service.ThumbsService;
import com.zs.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Created by zs on 2022/5/12.
 */
@Service
public class ThumbsServiceImpl implements ThumbsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThumbsServiceImpl.class);

    @Resource
    private ThumbsRedisHelper thumbsRedisHelper;

    @Resource
    private FansRedisHelper fansRedisHelper;

    @Resource
    private BlogOutlineMapper blogOutlineMapper;

    @Resource
    private BlogMapper blogMapper;

    /**
     * 点赞
     * @param thumbsDTO 点赞数据
     * @return code = 200 成功
     *         code = 525 失败
     */
    @Override
    public ResultVO like(ThumbsDTO thumbsDTO) throws JsonProcessingException {
        String key = ConstRedisKeyPrefix.ARTICLE_NEW_LIKES_PREFIX + thumbsDTO.getBid();
        String value = String.valueOf(thumbsDTO.getMid());
        // 是否已赞
        Boolean isLike = thumbsRedisHelper.isLike(key, value);
        if (isLike) {
            return new ResultVO(Const2.ALREADY_LIKE, "already like", null);
        }
        // 点赞
        Boolean likeResult = thumbsRedisHelper.addLike(key, value);
        if (likeResult) {
            // 点赞数累加
            thumbsRedisHelper.incrLikeNum(ConstRedisKeyPrefix.ARTICLE_ALL_LIKES_PREFIX + thumbsDTO.getBid());
            return new ResultVO(Const2.SERVICE_SUCCESS, "success", null);
        }
        return new ResultVO(Const2.SERVICE_FAIL, "fail", null);
    }

    /**
     * 获取文章点赞数
     * @param bid 文章id
     * @return
     */
    @Override
    public ResultVO getLikesByBid(Long bid) throws JsonProcessingException {
        String key = ConstRedisKeyPrefix.ARTICLE_ALL_LIKES_PREFIX + bid;
        Long articleLikeNum = thumbsRedisHelper.getArticleLikeNum(key);
        if (articleLikeNum == null) {
            // 需查询数据库
            BlogOutline blogOutline = blogOutlineMapper.getBlogOutlineByBid(bid);
            thumbsRedisHelper.cacheArticleLikeNum(key, String.valueOf(blogOutline.getLikeNum()));
            return new ResultVO(Const2.SERVICE_SUCCESS, "query success", blogOutline.getLikeNum());
        }
        return new ResultVO(Const2.SERVICE_SUCCESS, "query success", articleLikeNum);
    }

    /**
     * 判断用户是否已为当前文章点赞
     * @param thumbsDTO 点赞信息
     * @return
     */
    @Override
    public ResultVO isLike(ThumbsDTO thumbsDTO) throws JsonProcessingException {
        String key = ConstRedisKeyPrefix.ARTICLE_NEW_LIKES_PREFIX + thumbsDTO.getBid();
        String value = String.valueOf(thumbsDTO.getMid());

        Boolean isLike = thumbsRedisHelper.isLike(key, value);
        if (isLike) {
            return new ResultVO(Const2.ALREADY_LIKE, "already like", null);
        }
        return new ResultVO(Const2.NO_LIKE, "no like", null);
    }

    /**
     * 取消点赞
     * @param thumbsDTO
     * @return  code == 756 取消成功
     *          code == 753 取消失败，该用户还未点赞
     */
    @Override
    public ResultVO cancelLike(ThumbsDTO thumbsDTO) {
        /*
            先判断是否已经点赞，如果为true则取消点赞，否则不做操作
            取消点赞分两步：
                1.删除articleNewLikes_bid记录
                2.articleAllLikes_bid 减一
        */
        String key = ConstRedisKeyPrefix.ARTICLE_NEW_LIKES_PREFIX + thumbsDTO.getBid();
        String value = String.valueOf(thumbsDTO.getMid());
        Boolean isLike = thumbsRedisHelper.isLike(key, value);
        if (isLike) {
            thumbsRedisHelper.removeLike(key, value);
            thumbsRedisHelper.decrLikeNum(ConstRedisKeyPrefix.ARTICLE_ALL_LIKES_PREFIX + thumbsDTO.getBid());
            return new ResultVO(Const2.CANCEL_LIKE_SUCCESS, "cancel like success", null);
        }
        return new ResultVO(Const2.NO_LIKE, "current user not like this article", null);
    }

    /**
     * 同步点赞数量
     *   1.获取所有以 articleAllLikes_ 为前缀的key；
     *   2.遍历key，获取value；
     *   3.修改 tb_blog、tb_blog_outline表对应字段记录。
     * @return
     */
    @Override
    public ResultVO syncLike() {
        // step1
        Set<String> keys = fansRedisHelper.getPatternKey(ConstRedisKeyPrefix.ARTICLE_ALL_LIKES_PREFIX + "*");
        if (Objects.isNull(keys)) {
            return new ResultVO(Const2.SYNC_SUCCESS_NO_OPERATION, "success,no operation", null);
        }
        // step2
        BlogOutline condition1 = new BlogOutline();
        Blog condition2 = new Blog();
        keys.stream().forEach((key) -> {
            Long articleLikeNum = thumbsRedisHelper.getArticleLikeNum(key);
            if (!Objects.isNull(articleLikeNum)) {
                // step3
                long articleId = Long.parseLong(key.split("_")[1]);
                condition1.setDid(articleId);
                condition1.setLikeNum(articleLikeNum);
                blogOutlineMapper.updateByCondition(condition1);
                condition2.setBid(articleId);
                condition2.setLikeNum(articleLikeNum);
                blogMapper.updateBlogById(condition2, articleId);
            }
        });
        return new ResultVO(Const2.SYNC_SUCCESS, "sync success", keys.size());
    }
}
