package com.zs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zs.config.Const;
import com.zs.config.Const2;
import com.zs.handler.ElasticSearchUtils;
import com.zs.mapper.BlogOutlineMapper;
import com.zs.mapper.CategoryMapper;
import com.zs.mapper.WriterMapper;
import com.zs.pojo.Category;
import com.zs.pojo.Writer;
import com.zs.vo.BlogES;
import com.zs.vo.BlogOutlineVO;
import lombok.extern.slf4j.Slf4j;
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
        CreateIndexRequest indexRequest = new org.elasticsearch.client.indices.CreateIndexRequest("zblogarticlesindex");
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(indexRequest, RequestOptions.DEFAULT);
        Assert.isTrue(createIndexResponse.isAcknowledged(), "创建文章索引失败");
    }

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
        List<BlogES> search = elasticSearchUtils.search(Const2.ES_ARTICLE_INDEX, "thymeleaf", 0, BlogES.class, "title", "outline");
        search.stream().forEach(System.out::println);
    }
}
