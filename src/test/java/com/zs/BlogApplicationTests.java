package com.zs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zs.config.Const;
import com.zs.config.Const2;
import com.zs.config.ConstRedisKeyPrefix;
import com.zs.handler.ArticleRankRedisHelper;
import com.zs.handler.ArticleRedisHelper;
import com.zs.handler.ElasticSearchUtils;
import com.zs.mapper.BlogOutlineMapper;
import com.zs.mapper.CategoryMapper;
import com.zs.mapper.WriterMapper;
import com.zs.pojo.Category;
import com.zs.pojo.Writer;
import com.zs.service.BlogService;
import com.zs.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.Assert;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@EnableScheduling
@SpringBootTest
class BlogApplicationTests {

    @Autowired
    CategoryMapper categoryMapper;
    @Resource
    private BlogOutlineMapper blogOutlineMapper;
    @Resource
    private WriterMapper writerMapper;
    @Resource
    private RestHighLevelClient restHighLevelClient;
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private ElasticSearchUtils elasticSearchUtils;
    @Resource
    private ArticleRankRedisHelper articleRankRedisHelper;
    @Resource
    private BlogService blogService;

    @Test
    void contextLoads() {
//        log.info("datasource:{}", dataSource.getClass());

        String s = DigestUtils.md5Digest("323".getBytes(StandardCharsets.UTF_8)).toString();
        System.out.println("加密:" + new String(s.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    void page() {
        PageHelper.startPage(0,1);
        List<Category> categories = categoryMapper.listCategories();
        PageInfo<Category> info = new PageInfo<>(categories);
        System.out.println(info);
        System.out.println(info.getList());
    }

    @Test
    void listBlogOutlines() throws Exception {
        List<BlogOutlineVO> outlineVOS = blogOutlineMapper.listBlogOutlines();
        outlineVOS.stream().forEach(bo -> System.out.println(bo));
    }

    @Test
    void listWriter() throws Exception {
        Writer w = new Writer();
        w.setArticleNum(5);
        w.setWriterStatus(0);
        List<Writer> writers = writerMapper.listWriterByCondition(w);
        writers.stream().forEach(w1 -> System.out.println(w1));
    }

    @Test
    void testCreateESIndex() throws IOException {
        // 创建文章信息索引
//        CreateIndexRequest indexRequest = new org.elasticsearch.client.indices.CreateIndexRequest("zblogarticlesindex");
        // 创建用户信息索引
        CreateIndexRequest indexRequest = new org.elasticsearch.client.indices.CreateIndexRequest(Const2.ES_USERINFO_INDEX);
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(indexRequest, RequestOptions.DEFAULT);
        Assert.isTrue(createIndexResponse.isAcknowledged(), "创建文章索引失败");
    }

    @Test
    void testDeleteIndex() throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(Const2.ES_USERINFO_INDEX);
        restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
    }

    /**
     * 从数据库中导入文章基本信息到ES中做搜索
     * TODO 服务器内存不够
     * @throws IOException
     */
    @Test
    void testImportDataInDBToES() throws IOException {
        // 1.查询数据库中的所有数据
        List<BlogES> blogES = blogOutlineMapper.listESBlogs();
        // 2.添加到es索引中
        Iterator<BlogES> iterator = blogES.iterator();
        while (iterator.hasNext()) {
            BlogES next = iterator.next();
            IndexRequest indexRequest = new IndexRequest(Const2.ES_ARTICLE_INDEX);
            indexRequest.id(String.valueOf(next.getBid()));
            indexRequest.source(objectMapper.writeValueAsString(next), XContentType.JSON);
            IndexResponse index = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        }
    }

    @Test
    void testDeleteDocument() throws IOException {
        Boolean result = elasticSearchUtils.delDocument("index2", "101");
        Assert.isTrue(result, "删除失败");
    }

    @Test
    void testSearch() throws IOException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        List<BlogES> search = elasticSearchUtils.search(Const2.ES_ARTICLE_INDEX, "thymeleaf", 0, BlogES.class, "title", "outline");
//        search.stream().forEach(System.out::println);
        MyPageInfo<BlogES> pageInfo = elasticSearchUtils.search(Const2.ES_ARTICLE_INDEX, "thymeleaf", 0, BlogES.class, "title", "outline");
        System.out.println(pageInfo);
    }

    /**
     * 从ES中获取文章信息，并根据其得分，导入redis中做排名
     * @throws IOException
     */
    @Test
    void testImportRankDataToRedis() throws IOException {
        List<BlogES> blogs = elasticSearchUtils.searchAll(Const2.ES_ARTICLE_INDEX, BlogES.class);
        Iterator<BlogES> iterator = blogs.iterator();
        while (iterator.hasNext()) {
            BlogES next = iterator.next();
            double score = next.getViews() + next.getLikeNum();
            next.setOutline(null);
            articleRankRedisHelper.addToRank(ConstRedisKeyPrefix.ARTICLE_RANK, next, score);
        }
    }

    @Test
    void testZrevrange() throws JsonProcessingException {
        articleRankRedisHelper.getRankTopN(ConstRedisKeyPrefix.ARTICLE_RANK, 0 , 3);
    }

    /**
     * 将数据库的用户数据导入到ES中
     * @throws Exception
     */
    @Test
    void testImportWriterDataToES() throws Exception {
        WriterES writerES = new WriterES();
        List<Writer> writers = writerMapper.listWriterByCondition(new Writer());
        writers.stream().forEach(w -> {
            writerES.setUid(w.getUid());
            writerES.setWriterName(w.getWriterName());
            writerES.setWriterAvatar(w.getWriterAvatar());
            writerES.setFans(w.getFans());
            writerES.setArticleNum(w.getArticleNum());
            writerES.setAllViews(w.getAllViews());
            writerES.setAllLikeNums(w.getAllLikeNums());
            try {
                IndexRequest indexRequest = new IndexRequest(Const2.ES_USERINFO_INDEX);
                indexRequest.id(String.valueOf(writerES.getUid()));
                indexRequest.source(objectMapper.writeValueAsString(writerES), XContentType.JSON);
                restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    void testSearchWriterInES() {
//        ResultVO search = blogService.search("花", 1, "userInfo");
        ResultVO search = blogService.search("mybatis", 1, null);
        System.out.println(search);
    }

}
