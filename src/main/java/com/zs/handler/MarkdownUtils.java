package com.zs.handler;

import com.zs.config.Const;
import com.zs.pojo.BlogOutline;
import org.apache.commons.lang3.StringUtils;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TableBlock;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.AttributeProvider;
import org.commonmark.renderer.html.AttributeProviderContext;
import org.commonmark.renderer.html.AttributeProviderFactory;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 通用工具类
 * @Created by zs on 2022/3/6.
 */
public class MarkdownUtils {

    /**
     * 语法解析工具，将 Markdown 语法的字符串解析为只有 '汉字'、'数字'、'字母','常用标点符号'
     * @param data 原字符串
     * @return
     */
    public static String wordParse(String data) {
        // 正则
        Pattern chineseReg = Pattern.compile("[\u4e00-\u9fa5]");
        Pattern numberReg = Pattern.compile("[0-9]"); // 48 - 57
        Pattern letterReg = Pattern.compile("[a-z]");
        Pattern punctuationReg = Pattern.compile("[!！￥%…&（）？—+,./?^$]");

        // 将String转为集合
        List<String> listOrigin = new LinkedList<>();
        if (data.length() >= Const.BLOG_OUTLINE_NUM) {
            for (int i = 0; i < Const.BLOG_OUTLINE_NUM; i++) {
                listOrigin.add(data.charAt(i) + "");
            }
        } else {
            for (int i = 0; i < data.length(); i++) {
                listOrigin.add(data.charAt(i) + "");
            }
        }
        // 字符过滤
        Stream<String> stringStream = listOrigin.stream().filter(s -> chineseReg.matcher(s).find() == true ||
                                                                      numberReg.matcher(s).find() == true ||
                                                                      punctuationReg.matcher(s).find() == true ||
                                                                      letterReg.matcher(s).find() == true);
        List<String> listParse = stringStream.collect(Collectors.toList());
        // 将过滤后的集合转回String
        String parseData = StringUtils.join(listParse, "");
        return parseData;
    }


    /**
     * 将 Markdown 转为 Html
     * @param markdownData
     * @return
     */
    public static String markdownToHtml(String markdownData) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdownData);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    /**
     * 将 Markdown 转为 Html
     * 扩展 : 增加锚点，表格生成
     * @param markdownData
     * @return
     */
    public static String markdownToHtmlExtensions(String markdownData) {
        Set<Extension> headingAnchorExtension = Collections.singleton(HeadingAnchorExtension.create());
        List<Extension> tablesExtension = Arrays.asList(TablesExtension.create());
        Parser parser = Parser.builder().extensions(tablesExtension).build();
        Node document = parser.parse(markdownData);
        HtmlRenderer renderer = HtmlRenderer.builder()
                .extensions(headingAnchorExtension)
                .extensions(tablesExtension)
                .attributeProviderFactory(new AttributeProviderFactory() {
                    @Override
                    public AttributeProvider create(AttributeProviderContext attributeProviderContext) {
                        return new CustomAttributeProvider();
                    }
                }).build();
        return renderer.render(document);
    }

    /**
     * 定制规则
     */
    static class CustomAttributeProvider implements  AttributeProvider {
        @Override
        public void setAttributes(Node node, String s, Map<String, String> map) {
            // 如果内容中有超链接，则设置属性为点击后跳转页面
            if (node instanceof Link) {
                map.put("target", "_blank");
            }
            // 将表格添加class样式
            if (node instanceof TableBlock) {
                map.put("class", "ui celled table");
            }
        }
    }



    /**
     * 返回浏览量前 5 篇博客对应的概要对象
     * @param list
     * @return
     * -------------弃用---------------
     */
    public static List<BlogOutline> sortByViews(List<BlogOutline> list) {
        list.sort(new Comparator<BlogOutline>() {
            @Override
            public int compare(BlogOutline o1, BlogOutline o2) {
                return (int) (o2.getViews() - o1.getViews());
            }
        });
        if (list.size() >= 5) {
            return list.subList(0,4);
        } else {
            return list;
        }
    }


}
