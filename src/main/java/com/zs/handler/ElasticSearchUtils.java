package com.zs.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zs.config.Const;
import com.zs.vo.BlogES;
import com.zs.vo.MyPageInfo;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * ES工具类
 * @Created by zs on 2022/5/22.
 */
@Component("elasticSearchUtils")
public class ElasticSearchUtils {

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Resource
    private ObjectMapper objectMapper;

    /**
     * 添加文档
     * @param index ES索引名
     * @param docId 文档id
     * @param obj 文档数据
     * @return
     */
    public Boolean addDocument(String index, String docId, Object obj) throws IOException {
        IndexRequest indexRequest = new IndexRequest(index);
        indexRequest.id(docId);
        indexRequest.source(objectMapper.writeValueAsString(obj), XContentType.JSON);
        IndexResponse response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        if (response.getResult() == DocWriteResponse.Result.CREATED) {
            return true;
        }
        return false;
    }

    /**
     * 删除文档
     * @param index ES索引名
     * @param docId 文档id
     * @return
     */
    public Boolean delDocument(String index, String docId) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(index);
        deleteRequest.id(docId);
        DeleteResponse response = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        if (response.getResult() == DocWriteResponse.Result.DELETED) {
            return true;
        }
        return false;
    }

    /**
     * 检索记录
     * @param index ES索引
     * @param keyword 检索关键词
     * @param start 起始记录下标
     * @param t 检索到结果后需转换的对象类型
     * @param fieldName 需检索的field,该参数的第一个field将被设置为高亮，且只有一个高亮field
     * @param <T>
     * @return
     */
//    public <T> List<T> search(String index, String keyword, int start, Class<T> t, String... fieldName) throws IOException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        SearchRequest searchRequest = new SearchRequest(index);
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        // 封装检索条件
//        searchSourceBuilder.query(QueryBuilders.multiMatchQuery(keyword, fieldName));
//        // 设置分页
//        searchSourceBuilder.from(start);
//        searchSourceBuilder.size(Const.CATEGORY_PAGE_ROWS);
//        // 封装高亮条件
//        HighlightBuilder highlightBuilder = new HighlightBuilder();
//        HighlightBuilder.Field field = new HighlightBuilder.Field(fieldName[0]);
//        highlightBuilder.field(field);
//        highlightBuilder.preTags("<label style='color: red;'>");
//        highlightBuilder.postTags("</label>");
//        searchSourceBuilder.highlighter(highlightBuilder);
//        // 开始检索
//        searchRequest.source(searchSourceBuilder);
//        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
//        // 封装检索结果
//        SearchHits hits = searchResponse.getHits();
//        long value = hits.getTotalHits().value;
//        if (value == 0) {
//            return null;
//        }
//        List<T> list = new ArrayList<>();
//        Iterator<SearchHit> iterator = hits.iterator();
//        while (iterator.hasNext()) {
//            SearchHit searchHit = iterator.next();
//            String sourceAsString = searchHit.getSourceAsString();
//            T t1 = objectMapper.readValue(sourceAsString, t);
//            // 高亮文本替换
//            Map<String, HighlightField> highlightFieldsMap = searchHit.getHighlightFields();
//            Set<Map.Entry<String, HighlightField>> entries = highlightFieldsMap.entrySet();
//            Iterator<Map.Entry<String, HighlightField>> entryIterator = entries.iterator();
//            while (entryIterator.hasNext()) {
//                Map.Entry<String, HighlightField> next = entryIterator.next();
//                String key = next.getKey();
//                Class<?> t1Class = t1.getClass();
//
//                // 反射获取对象与高亮field对应的属性
//                Field declaredField = t1Class.getDeclaredField(key);
//                Class<?> type = declaredField.getType();
//                //  获取对应的set方法
//                char pre = (char)(key.charAt(0) - 32);
//                String methodName = "set" + pre + key.substring(1);
//                // 替换高亮内容
//                Method method = t1Class.getDeclaredMethod(methodName, type);
//                method.invoke(t1, next.getValue().fragments()[0].toString());
//            }
//            list.add(t1);
//        }
//        // 返回
//        return list;
//    }

    
    public <T> MyPageInfo<T> search(String index, String keyword, int start, Class<T> t, String... fieldName) throws IOException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 封装检索条件
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery(keyword, fieldName));
        // 设置分页
        searchSourceBuilder.from(start);
        searchSourceBuilder.size(Const.CATEGORY_PAGE_ROWS);
        // 封装高亮条件
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        HighlightBuilder.Field field = new HighlightBuilder.Field(fieldName[0]);
        highlightBuilder.field(field);
        highlightBuilder.preTags("<label style='color: red;'>");
        highlightBuilder.postTags("</label>");
        searchSourceBuilder.highlighter(highlightBuilder);
        // 开始检索
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        // 封装检索结果
        SearchHits hits = searchResponse.getHits();
        long value = hits.getTotalHits().value;
        if (value == 0) {
            return null;
        }
        MyPageInfo<T> pageInfo = new MyPageInfo<>();
        pageInfo.setStart(start);
        pageInfo.setTotal(value);
        pageInfo.setPageSize(Const.CATEGORY_PAGE_ROWS);
        int totalPage = (int) (value % Const.CATEGORY_PAGE_ROWS == 0 ? value / Const.CATEGORY_PAGE_ROWS :  value / Const.CATEGORY_PAGE_ROWS + 1);
        pageInfo.setTotalPage(totalPage);

        List<T> list = new ArrayList<>();
        Iterator<SearchHit> iterator = hits.iterator();
        while (iterator.hasNext()) {
            SearchHit searchHit = iterator.next();
            String sourceAsString = searchHit.getSourceAsString();
            T t1 = objectMapper.readValue(sourceAsString, t);
            // 高亮文本替换
            Map<String, HighlightField> highlightFieldsMap = searchHit.getHighlightFields();
            Set<Map.Entry<String, HighlightField>> entries = highlightFieldsMap.entrySet();
            Iterator<Map.Entry<String, HighlightField>> entryIterator = entries.iterator();
            while (entryIterator.hasNext()) {
                Map.Entry<String, HighlightField> next = entryIterator.next();
                String key = next.getKey();
                Class<?> t1Class = t1.getClass();

                // 反射获取对象与高亮field对应的属性
                Field declaredField = t1Class.getDeclaredField(key);
                Class<?> type = declaredField.getType();
                //  获取对应的set方法
                char pre = (char)(key.charAt(0) - 32);
                String methodName = "set" + pre + key.substring(1);
                // 替换高亮内容
                Method method = t1Class.getDeclaredMethod(methodName, type);
                method.invoke(t1, next.getValue().fragments()[0].toString());
            }
            list.add(t1);
        }
        pageInfo.setList(list);
        // 返回
        return pageInfo;
    }

}
