package com.zs.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zs.config.Const;
import com.zs.config.Const2;
import com.zs.config.ConstRedisKeyPrefix;
import com.zs.handler.*;
import com.zs.mapper.*;
import com.zs.pojo.*;
import com.zs.service.BlogService;
import com.zs.vo.BlogES;
import com.zs.vo.BlogVO;
import com.zs.vo.MyPageInfo;
import com.zs.vo.ResultVO;
import org.aspectj.weaver.ast.Var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @Created by zs on 2022/3/3.
 */
@Service
public class BlogServiceImpl implements BlogService {

    private Logger logger = LoggerFactory.getLogger(BlogServiceImpl.class);

    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Resource
    private BlogOutlineMapper blogOutlineMapper;
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private WriterMapper writerMapper;
    @Resource
    private ArticleRedisHelper articleRedisHelper;
    @Resource
    private ThumbsRedisHelper thumbsRedisHelper;
    @Resource
    private CopyrightMapper copyrightMapper;
    @Resource
    private ElasticSearchUtils elasticSearchUtils;

    @Override
    public PageInfo<Blog> listPageBlogs(Integer currentPage, Integer rows) {
        PageHelper.startPage(currentPage, rows);
        List<Blog> listBlogs = blogMapper.listBlogs();
        PageInfo<Blog> pageInfo = new PageInfo<>(listBlogs);
        return pageInfo;
    }

    /**
     * 条件分页查询博客
     * @param currentPage 页码
     * @param rows 每页行数
     * @param title 博客标题
     * @param categoryId  博客所属分类id
     * @param isPublish  是否发布
     * @return
     */
    @Override
    public PageInfo<Blog> pageConditionBlog(Integer currentPage, Integer rows,
                                            String title, Integer categoryId, Boolean isPublish) {
        List<Blog> listConditionBlogs = null;
        PageHelper.startPage(currentPage, rows);
        if (categoryId == 0) {
            listConditionBlogs = blogMapper.listConditionBlogs(title, null, isPublish);
        } else {
            listConditionBlogs = blogMapper.listConditionBlogs(title, categoryId, isPublish);
        }
        PageInfo<Blog> pageInfo = new PageInfo<>(listConditionBlogs);
        return pageInfo;
    }

    /**
     * 按博客id删除博客
     *   1、删除博客对应的留言信息
     *   2、删除博客的概要信息
     *   3、删除博客信息
     * @param bid
     * @return
     */
    @Transactional
    @Override
    public RequestResult deleteBlogById(Integer bid) {
        RequestResult requestResult = new RequestResult();
        commentMapper.deleteCommentByBid(bid);
        blogOutlineMapper.deleteByBid(bid);
        blogMapper.deleteBlogById(bid);
        requestResult.setCode(Const.DELETE_BLOG_SUCCESS);
        requestResult.setMessage("删除成功");
        return requestResult;
    }

    /**
     * 保存一条博客
     * 分为两个步骤: 一是保存博客，返回生成主键
     *            二是解析部分博客内容，生成博客概要对象，并插入数据库
     * @param blog 博客内容
     * @param loginUser 发布作者
     * @return
     */
    @Transactional
    @Override
    public RequestResult insertBlog(Blog blog, User loginUser) {
        RequestResult requestResult = new RequestResult();
        blog.setViews(0L);
        // 保存博客
        int rows = blogMapper.insertBlog(blog, loginUser);
        // 保存博客概要信息
        BlogOutline blogOutline = new BlogOutline();
        blogOutline.setDid(blog.getBid());
        blogOutline.setViews(blog.getViews());
        blogOutline.setTitle(blog.getTitle());
        blogOutline.setOutline(MarkdownUtils.wordParse(blog.getContent()));
        blogOutlineMapper.insert(blogOutline);

        if (rows == 1 && blog.getIsPublish()) {
            requestResult.setCode(Const.EDIT_BLOG_SUCCESS);
            requestResult.setMessage("博客发布成功");
        } else {
            requestResult.setCode(Const.EDIT_BLOG_SUCCESS);
            requestResult.setMessage("博客已保存至草稿");
        }
        return requestResult;
    }

    /**
     * 查询博客
     * @param bid
     * @return
     */
    @Override
    public ResultVO getBlogById(Long bid) {
        if (bid == null) {
            return new ResultVO(Const2.SERVICE_FAIL, "fail: article id is null", null);
        }
        try {
            BlogVO blogById = blogMapper.getBlogById(bid);
            return new ResultVO(Const2.SERVICE_SUCCESS, "success", blogById);
        } catch (Exception e) {
            logger.info("查询文章信息异常");
            e.printStackTrace();
            return new ResultVO(Const2.SERVICE_FAIL, "exception", null);
        }
    }

    /**
     * 按照博客id编辑博客
     *      需先根据bid查询博客记录，比较记录是否全部相等，
     *      务必重写Blog实体类的 hashCode() and equals()方法，
     *      重写方法时有目的的选择比较的属性，不必比较全部属性
     * @param blog
     * @param bid
     * @return
     */
    @Transactional
    @Override
    public RequestResult updateBlog(Blog blog, Long bid) throws Exception {
        RequestResult requestResult = new RequestResult();
        /* 获取原博客内容 */
        Blog tmp = new Blog();
        tmp.setBid(bid);
        Blog blogById = blogMapper.getBlog(tmp);
        // 数据库数据和表单数据比较，返回的 needUpdateData 里面只包括需要修改的属性项，其余为 null
        Blog needUpdateData = CompareUtils.compareBlog(blogById, blog);
        if (needUpdateData.getBid() == null && needUpdateData.getTitle() == null &&
            needUpdateData.getContent() == null && needUpdateData.getCid() == null &&
            needUpdateData.getIsAppreciate() == null && needUpdateData.getIsComment() == null &&
            needUpdateData.getIsPublish() == null && needUpdateData.getCrTipId() == null) {
            /* 博客内容无修改、只更新博客首图 */
            if (!(blog.getFirstPicture() == null || "".equals(blog.getFirstPicture()))) {
                Blog updatePicture = new Blog();
                updatePicture.setFirstPicture(blog.getFirstPicture());
                blogMapper.updateBlogById(updatePicture, bid);
                requestResult.setCode(Const.EDIT_BLOG_SUCCESS);
                requestResult.setMessage("图片更新成功");
            } else {
                requestResult.setCode(Const.EDIT_BLOG_FAILED_CONTENT_IS_NULL);
                requestResult.setMessage("编辑失败 : 无更改项");
            }
        } else {
            /* 博客内容修改 */
            needUpdateData.setFirstPicture(blog.getFirstPicture());
            blogMapper.updateBlogById(needUpdateData, bid);
            requestResult.setCode(Const.EDIT_BLOG_SUCCESS);
            requestResult.setMessage("博客编辑成功");
        }
        return requestResult;
    }

    /**
     * 查询浏览量前 10 的文章概要
     * @return
     */
    @Override
    public List<BlogOutline> listRecommendBlog() {
        List<BlogOutline> recommendList = blogOutlineMapper.listSortByViewsBlogOutline();
        if (recommendList.size() > 10) {
            return recommendList.subList(0,10);
        }
        return recommendList;
    }

    /**
     * 查询博客，并将内容由 Markdown 转为 HTML（2.0）
     * @param bid
     * @return
     */
    @Override
    public ResultVO getBlogByIdAndConvert(Long bid) {
        if (bid == null) {
            return new ResultVO(Const2.SERVICE_FAIL, "fail: article id is null", null);
        }
        try {
            // 1.先查询缓存是否有文章信息
            BlogVO blogVOByBidToRedis = articleRedisHelper.getArticleCacheByBid(ConstRedisKeyPrefix.ALL_ARTICLE_CACHE, bid + "");
            if (blogVOByBidToRedis != null) {
                // 有缓存时，将redis中最新的文章点赞数量更新
                Long articleLikeNum = thumbsRedisHelper.getArticleLikeNum(ConstRedisKeyPrefix.ARTICLE_ALL_LIKES_PREFIX + bid);
                if (articleLikeNum != null) {
                    blogVOByBidToRedis.getBlog().setLikeNum(articleLikeNum);
                }
                return new ResultVO(Const2.SERVICE_SUCCESS, "success", blogVOByBidToRedis);
            }
            // 2.1 查询文章信息
            BlogVO blogVO = new BlogVO();
            Blog blog =  blogMapper.getBlogByBid(bid);
            if (blog == null) {
                return new ResultVO(Const2.SERVICE_FAIL, "article not found", null);
            }
            String htmlData = MarkdownUtils.markdownToHtmlExtensions(blog.getContent());
            blog.setContent(htmlData);
            blogVO.setBlog(blog);
            // 2.1.1 将文章点赞数缓存到redis中
            String key = ConstRedisKeyPrefix.ARTICLE_ALL_LIKES_PREFIX + blog.getBid();
            String value = String.valueOf(blog.getLikeNum());
            thumbsRedisHelper.cacheArticleLikeNum(key, value);
            // 2.2 查询作者信息
            Writer condition1 = new Writer();
            condition1.setUid(blog.getUid());
            List<Writer> writers = writerMapper.listWriterByCondition(condition1);
            if (writers == null || writers.size() == 0) {
                return new ResultVO(Const2.SERVICE_FAIL, "writer not found", null);
            }
            writers.get(0).setPwd(null);
            blogVO.setWriter(writers.get(0));
            // 2.3 查询文章分类信息
            Category condition2 = new Category();
            condition2.setCid(blog.getCid());
            Category category = categoryMapper.getCategory(condition2);
            if (category == null) {
                return new ResultVO(Const2.SERVICE_FAIL, "article category not found", null);
            }
            blogVO.setCategory(category);
            // 2.4 查询版权信息
            Copyright copyright = copyrightMapper.getCopyRightByCrTipId(blog.getCrTipId());
            blogVO.setCopyright(copyright);
            // 2.5 查询当前用户的热门文章概要信息5篇
            List<BlogOutline> top5ByViews = blogOutlineMapper.topNArticlesViewedByUid(blog.getUid(), 5);
            blogVO.setHotArticlesList(top5ByViews);
            // 2.6 缓存到redis中
            articleRedisHelper.cacheArticle(ConstRedisKeyPrefix.ALL_ARTICLE_CACHE, String.valueOf(bid), blogVO);
            return new ResultVO(Const2.SERVICE_SUCCESS, "success", blogVO);
        } catch (Exception e) {
            logger.info("查询文章信息异常");
            e.printStackTrace();
            return new ResultVO(Const2.SERVICE_FAIL, "exception", null);
        }
    }

    /**
     * 分页查询指定分类的博客
     * @param cid
     * @return
     */
    @Override
    public PageInfo<Blog> listPageBlogsByCid(Integer currentPage, Integer rows, Integer cid) {
        PageHelper.startPage(currentPage, rows);
        List<Blog> blogsByCid = blogMapper.listBlogs(cid);
        PageInfo<Blog> pageInfo = new PageInfo<>(blogsByCid);
        return pageInfo;
    }

    /**
     * 查询博客基本信息(留言管理显示)
     * @param bid
     * @return
     */
    @Override
    public Blog getBlogBaseMsg(Long bid) {
        return blogMapper.getBlogBaseMsg(bid);
    }

    /**
     * 发布文章（2.0）
     * @param blog 文章内容
     * @param firstPicture 文章封面
     * @param mail 用户邮箱
     * @return ResultVO.code = 5052 成功
     *         ResultVO.code = 5053 失败
     */
    @Transactional
    @Override
    public ResultVO addArticle(Blog blog, MultipartFile firstPicture, String mail) {
        // 1.处理上传文章封面
        ResultVO uploadResult = null;
        try {
            uploadResult = UploadUtils.uploadFileHandler(firstPicture, mail,
                                                         Const.BLOG_FIRST_PICTURE_SIZE,
                                                         Const.PICTURE_SUPPORT_FORMAT, 0);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("上传文件异常");
        }
        // 2.处理文章内容
        blog.setLikeNum(0L);
        blog.setViews(0L);
        blog.setWriteTime(new Date());
        blog.setFirstPicture((String) uploadResult.getData());
        blogMapper.insert(blog);
        // 3.生成文章概要对象
        BlogOutline blogOutline = new BlogOutline();
        blogOutline.setDid(blog.getBid());
        blogOutline.setTitle(blog.getTitle());
        blogOutline.setOutline(MarkdownUtils.wordParse(blog.getContent()));
        blogOutline.setViews(blog.getViews());
        blogOutline.setUid(blog.getUid());
        blogOutline.setLikeNum(blog.getLikeNum());
        blogOutline.setFirstPicture(blog.getFirstPicture());
        blogOutline.setWriteTime(blog.getWriteTime());
        blogOutline.setIsPublish(blog.getIsPublish());
        blogOutlineMapper.insert(blogOutline);
        try {
            // 4.用户文章数累加
            if (blog.getIsPublish()) {
                Writer condition = new Writer();
                condition.setUid(blog.getUid());
                List<Writer> writers = writerMapper.listWriterByCondition(condition);
                Writer writer = new Writer();
                writer.setUid(writers.get(0).getUid());
                writer.setArticleNum(writers.get(0).getArticleNum()+1);
                writerMapper.updateWriterByUid(writer);
            }
                return new ResultVO(Const.EDIT_BLOG_SUCCESS, "success", null);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("发布文章接口异常");
            return new ResultVO(Const.EDIT_BLOG_FAILED, "fail", null);
        }
    }

    /**
     * 更新文章状态
     * @param uid 用户id
     * @param bid 文章id
     * @param status 状态 1：发布 0：草稿
     * @return code == 200 成功
     *         code == 404 失败，查询不到对应文章信息
     *         code == 525 失败，抛出异常，文章id有误
     *         code == 301 失败，请求参数有误
     */
    @Transactional
    @Override
    public ResultVO updateArticleStatus(String bid, String status, String uid) {
        Boolean checkStatus = false;
        if (Objects.equals("1", status)) {
            checkStatus = true;
        } else if (Objects.equals("0", status)) {
            checkStatus = true;
        }
        if (checkStatus) {
            try {
                BlogOutline blogOutline = new BlogOutline();
                Boolean isPublish = Integer.parseInt(status) == 1 ? true : false;
                blogOutline.setIsPublish(isPublish);
                blogOutline.setDid(Long.parseLong(bid));
                int r1 = blogOutlineMapper.updateByCondition(blogOutline);
                Blog blog = new Blog();
                blog.setBid(Long.parseLong(bid));
                blog.setIsPublish(isPublish);
                int r2 = blogMapper.updateBlogById(blog, blog.getBid());
                if (r1 == 1 && r2 == 1) {
                    Writer condition = new Writer();
                    condition.setUid(Integer.parseInt(uid));
                    List<Writer> writers = writerMapper.listWriterByCondition(condition);
                    if (writers.size() == 1) {
                        Writer writer = new Writer();
                        writer.setUid(writers.get(0).getUid());
                        if (Objects.equals("1", status)) {
                            writer.setArticleNum(writers.get(0).getArticleNum() + 1);
                        } else if (Objects.equals("0", status)) {
                            writer.setArticleNum(writers.get(0).getArticleNum() - 1);
                        }
                        writerMapper.updateWriterByUid(writer);
                        return new ResultVO(Const2.SERVICE_SUCCESS, "success", null);
                    }
                }
                return new ResultVO(Const2.NOT_FOUND,  "parameter not found", null);
            } catch(Exception e) {
                e.printStackTrace();
                logger.info("更新文章状态接口异常");
                return new ResultVO(Const2.SERVICE_FAIL, "request params " + bid + " fail", null);
            }
        }
        return new ResultVO(Const2.PARAMETER_FAIL, "request params " + status + " fail", null);
    }

    /**
     * 删除文章(2.0)
     * @param uid 用户id
     * @param bid 文章id
     * @return
     */
    @Transactional
    @Override
    public ResultVO deleteBlogById2(String uid, String bid) {
        Integer writerId = null;
        Integer articleId = null;
        try {
            writerId = Integer.parseInt(uid);
            articleId = Integer.parseInt(bid);
        } catch(Exception e) {
            e.printStackTrace();
            logger.info("删除文章接口异常，请求参数有误");
            return new ResultVO(Const2.PARAMETER_FAIL, "error in request parameters", null);
        }
        try {
            // 1.删除文章所有评论
            commentMapper.deleteCommentByBid(articleId);
            // 2.删除文章概要
            blogOutlineMapper.deleteByBid(articleId);
            // 3.删除文章
            blogMapper.deleteBlogById(articleId);
            // 4.修改用户发布文章数
            Writer condition = new Writer();
            condition.setUid(writerId);
            List<Writer> writers = writerMapper.listWriterByCondition(condition);
            Writer writer = new Writer();
            writer.setUid(writers.get(0).getUid());
            writer.setArticleNum(writers.get(0).getArticleNum() - 1);
            writerMapper.updateWriterByUid(writer);
            return new ResultVO(Const2.SERVICE_SUCCESS, "success", null);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("删除文章接口异常，数据库异常");
            return new ResultVO(Const2.SERVICE_FAIL, "fail", null);
        }
    }

    /**
     * 查询指定文章markdown形式内容
     * @param uid 用户id
     * @param bid 文章id
     * @return
     */
    @Override
    public ResultVO getEditViewBlogById(String uid, String bid) {
        Long writerId = null;
        Long articleId = null;
        try {
            writerId = Long.parseLong(uid);
            articleId = Long.parseLong(bid);
        } catch (Exception e) {
            logger.info("查询文章编辑视图接口异常", e);
            return new ResultVO(Const2.SERVICE_FAIL, "parameter b not found", null);
        }
        try {
            Blog blog = blogMapper.getBlogByBIdAndUid(writerId, articleId);
            if (blog == null) {
                return new ResultVO(Const2.NOT_FOUND, "The article to be edited cannot be found", null);
            }
            return new ResultVO(Const2.SERVICE_SUCCESS, "success", blog);
        } catch (Exception e) {
            logger.info("查询文章编辑视图接口异常", e);
            return new ResultVO(Const2.SERVICE_FAIL, "fail", null);
        }
    }

    /**
     * 编辑文章（2.0）
     * @param blog 编辑后内容
     * @param file 上传封面
     * @return
     */
    @Transactional
    @Override
    public ResultVO updateBlog2(Blog blog, MultipartFile file) {
        // 1.重要参数验证
        if (blog.getBid() == null || blog.getUid() == null)
            return new ResultVO(Const2.SERVICE_FAIL, "request parameters is fail", null);
        // 2.处理上传文件
        ResultVO uploadResult = null;
        try {
            uploadResult = UploadUtils.uploadFileHandler(file, RandomUtils.generateRandomNum(),
                                                         Const.BLOG_FIRST_PICTURE_SIZE,
                                                         Const.PICTURE_SUPPORT_FORMAT, 0);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("上传文件异常");
        }
        if (Objects.equals(538, uploadResult.getCode())) {
            blog.setFirstPicture((String) uploadResult.getData());
        }
        // 3.更新文章数据
        blog.setUpdateTime(new Date());
        int r1 = blogMapper.updateBlogByBIdAndUid(blog);
        // 4.更新文章概要数据
        BlogOutline blogOutline = new BlogOutline();
        blogOutline.setDid(blog.getBid());
        blogOutline.setUid(blog.getUid());
        blogOutline.setOutline(MarkdownUtils.wordParse(blog.getContent()));
        blogOutline.setTitle(blog.getTitle());
        blogOutline.setFirstPicture(blog.getFirstPicture());
        int r2 = blogOutlineMapper.updateByBidAndUid(blogOutline);
        if (r1 == r2) {
            return new ResultVO(Const2.SERVICE_SUCCESS, "success", null);
        }
        return new ResultVO(Const2.SERVICE_FAIL, "fail", null);
    }

    /**
     * 内容搜索
     * @param keyword 关键词
     * @param p 页码
     * @return
     */
    @Override
    public ResultVO search(String keyword, int p) {
        try {
            int start = (p - 1) * Const.CATEGORY_PAGE_ROWS;
            List<BlogES> search = elasticSearchUtils.search(Const2.ES_ARTICLE_INDEX, keyword, start, BlogES.class, "title", "outline");
            if (Objects.isNull(search)) {
                return new ResultVO(Const2.SERVICE_SUCCESS, "no result", null);
            }
            PageInfo<BlogES> pageInfo = new PageInfo<>(search);
            // TODO 计算条数,el工具类返回一个自定义pageInfo对象

            return new ResultVO(Const2.SERVICE_SUCCESS, "success", pageInfo);
        } catch (Exception e) {
            logger.info("搜索接口异常{}", e);
        }
        return new ResultVO(Const2.SERVICE_FAIL, "exception", null);
    }
}
