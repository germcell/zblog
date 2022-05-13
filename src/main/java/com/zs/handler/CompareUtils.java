package com.zs.handler;

import com.zs.pojo.Blog;
import com.zs.pojo.Category;
import com.zs.pojo.Copyright;
import com.zs.pojo.User;
import lombok.ToString;

import java.lang.reflect.*;
import java.util.*;

/**
 * 比较工具类
 * @Created by zs on 2022/3/5.
 */
public class CompareUtils {

    /**
     * 比较两个Blog对象的值，返回不同的部分
     *
     *      场景 : 当用户编辑博客时，一般只有一部分内容会被改变，那么在更新数据库信息时，默认会将所有的重复信息重新插入一遍
     *            可调用此方法，来判断到底是哪些属性被用户更改了，然后将更改过的属性交给数据库更改，避免不必要的数据库访问
     *
     * @param b1 原对象 (顺序很重要) 数据库数据
     * @param b2 新对象            表单提交数据
     * @return
     * @throws Exception
     */
    public static Blog compareBlog(Blog b1, Blog b2) throws Exception {
        /* 排除比较的部分属性 */
        String excludeFields[] = {  "uid",
                                    "isReprint",
                                    "firstPicture",
                                    "views",
                                    "writeTime",
                                    "updateTime",
                                    "copyright",
                                    "user",
                                    "category",
                                    "listComments",
                                    "blogOutline"};
        List<String> listExcludeFields = Arrays.asList(excludeFields);

        Blog returnBlog = new Blog();
        Class<? extends Blog> returnBlogClass = returnBlog.getClass();
        Class<? extends Blog> b1Class = b1.getClass();
        Class<? extends Blog> b2Class = b2.getClass();
        Field[] returnBlogFields = returnBlogClass.getDeclaredFields();
        Field[] b1Fields = b1Class.getDeclaredFields();
        Field[] b2Fields = b2Class.getDeclaredFields();
        // 遍历比较对象1的所有属性 (控制循环次数)
        for (Field b1Field : b1Fields) {
            b1Field.setAccessible(true);
            // 遍历比较对象2的所有属性 (遍历)
            for (Field b2Field : b2Fields) {
                b2Field.setAccessible(true);
                String exclude = b2Field.getName();
                String type = b2Field.getGenericType().toString();
                // 将属性值转为对应的类型
                Object attrValue = null;
                switch (type) {
                    case "class java.lang.Long":
                        attrValue = (Long) b2Field.get(b2);
                        break;
                    case "class java.lang.String":
                        attrValue = (String) b2Field.get(b2);
                        break;
                    case "class java.lang.Integer":
                        attrValue = (Integer) b2Field.get(b2);
                        break;
                    case "class java.lang.Boolean":
                        attrValue = (Boolean) b2Field.get(b2);
                        break;
                    case "class java.util.Date":
                        attrValue = (Date) b2Field.get(b2);
                        break;
                    case "class com.zs.pojo.Copyright":
                        attrValue = (Copyright) b2Field.get(b2);
                        break;
                    case "class com.zs.pojo.User":
                        attrValue = (User) b2Field.get(b2);
                        break;
                    case "class com.zs.pojo.Category":
                        attrValue = (Category) b2Field.get(b2);
                        break;
                    case "interface java.util.List":
                        attrValue = (List) b2Field.get(b2);
                        break;
                }
                // 判断比较对象2的属性是否在排除属性中
                if (!listExcludeFields.contains(exclude)) {
                    if (b1Field.getName() == b2Field.getName()) {
                        // 判断比较对象1的属性值和比较对象2的属性值是否相同
                        if (!Objects.equals(b1Field.get(b1), b2Field.get(b2))) {
                            // 遍历返回对象的属性,命中相同的属性后,并将比较对象2的属性值赋值给它
                            for (Field returnBlogField : returnBlogFields) {
                                returnBlogField.setAccessible(true);
                                if (Objects.equals(exclude, returnBlogField.getName())) {
                                    returnBlogField.set(returnBlog, attrValue);
                                    break;
                                }
                            }
                        }
                    }
                    // FIXME 可以根据变量属性一一对应的关系，来尽早的结束循环，避免无用的循环次数，以减少耗时
                }
            }
        }
        return returnBlog;
    }

    /* 测试 */
    public static void main(String[] args) throws Exception {
        Blog b1 = new Blog();
        b1.setBid(2L);
        b1.setTitle("b1的title");
        b1.setWriteTime(new Date());
        b1.setIsComment(true);
        b1.setContent("4444444");
        b1.setFirstPicture("img1");

        Blog b2 = new Blog();
        b2.setBid(2L);
        b2.setTitle("b1的title");
        b2.setWriteTime(new Date());
        b2.setIsComment(true);
        b2.setContent(" ");
        b2.setFirstPicture("img2");

        Long begin =  System.currentTimeMillis();
        System.out.println(compareBlog(b1, b2));
        Long end =  System.currentTimeMillis();
        System.out.println("耗时: " + (end - begin));
//        System.out.println(compareObj(b1, b2));
    }

}
