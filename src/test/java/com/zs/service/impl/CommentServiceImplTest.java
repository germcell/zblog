package com.zs.service.impl;

import com.zs.mapper.CommentMapper;
import com.zs.pojo.Comment;
import com.zs.pojo.RequestResult;
import com.zs.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Created by zs on 2022/3/11.
 */
@SpringBootTest
class CommentServiceImplTest {

    @Resource
    private CommentService commentService;

    @Test
    void postComments() {
//        Comment comment = new Comment(45,"测试","111@qq.com","111","/fs",new Date(), 2332L, 23L, null, null, null);
//        int result = commentService.postComments(comment);
//        assertEquals(1, result);
    }

    @Test
    void listCommentsByBid() {
        List<Comment> comments = commentService.listCommentsByBid(44L);
        Iterator<Comment> iterator = comments.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    @Test
    void deleteComments() {
        // 单例留言删除测试
        //  Long[] ids = {47L};
        //  RequestResult requestResult = commentService.deleteComments(ids, 1L);
        //  System.out.println(requestResult);

        // 批量留言删除测试
        Long[] ids = {47L, 48L};
        RequestResult requestResult = commentService.deleteComments(ids);
        System.out.println(requestResult);
    }

    @Test
    void passComments() {
        Comment comment = new Comment();
        comment.setIsPass(true);
        comment.setPassContent("此留言违反了社区规范，已被屏蔽");
        Long[] comIds = {};
        RequestResult requestResult = commentService.passComments(comIds, comment);
        assertEquals(7004, requestResult.getCode());
    }

    @Resource
    private HashMap<String,List<String>> imageMap;

    @Test
    void mapTest() {
        imageMap.put("1", new LinkedList<>());
        System.out.println(imageMap);
        imageMap.remove("1");
        System.out.println(imageMap.get("1"));
    }

    @Test
    void imageRegTest() {
        // ![](/res/images/blogPicture/mushroom1647781677830.png)

        String testStr = "#### **1、引言**\n" +
                ">使用editormd + springboot实现md编辑器的图片上传功能\n" +
                "\n" +
                "#### **2、editormd部分**\n" +
                "2.1通过js开启上传图片功能\n" +
                ">imageFormats : 指定可接收的图片格式(我只列举了我工程所用到的)\n" +
                ">imageUploadURL : 后端处理图片的请求\n" +
                "\n" +
                "```jquery\n" +
                " imageUpload: true,\n" +
                " imageFormats: [\"jpg\", \"jpeg\", \"png\"],\n" +
                " imageUploadURL: \"http://127.0.0.1:8080/editormd/images\",\n" +
                " onload: function () {\n" +
                " console.log(\"begin upload\");\n" +
                " }\n" +
                "```\n" +
                "#### **3、SpringBoot部分**\n" +
                "3.1 json实体类封装\n" +
                ">因为editormd上传图片后需要服务端返回一个json数据，来判断是否上传成功，并且类型、属性名必须与editormd规定的一致，也就是下述的实体类。\n" +
                "\n" +
                "```java\n" +
                "public class EditorJson {\n" +
                "    /**\n" +
                "     * editormd上传图片返回的json数据\n" +
                "     */\n" +
                "    private int success;  // 后端是否处理成功 1成功 0失败\n" +
                "    private String message; // 提示信息\n" +
                "    private String url; // 成功后的url地址（图片存储在服务器的地址，相对地址）\n" +
                "}\n" +
                "```\n" +
                "\n" +
                "3.2 请求处理\n" +
                ">注意：请求参数设置为 editormd-image-file\n" +
                "\n" +
                "```java\n" +
                " @PostMapping(\"/editormd/images\")\n" +
                " @ResponseBody\n" +
                "    public EditorJson imagesHandler(@RequestParam(\"editormd-image-file\") MultipartFile file) {\n" +
                "        EditorJson result = new EditorJson();\n" +
                "        try {\n" +
                "            String accessDir = UploadUtils.uploadPictureHandler(file);\n" +
                "            if (accessDir == null || Objects.equals(\"\", accessDir)) {\n" +
                "                result.setSuccess(0);\n" +
                "                result.setMessage(\"错误 : 只支持5MB的jpg,jpeg,png格式的图片\");\n" +
                "            } else {\n" +
                "                result.setSuccess(1);\n" +
                "                result.setMessage(\"upload success\");\n" +
                "                result.setUrl(\"/res/images/\" + accessDir);\n" +
                "            }\n" +
                "        } catch (Exception e) {\n" +
                "            throw new UniversalException(\"图片上传失败,格式或大小错误\");\n" +
                "        }\n" +
                "        return result;\n" +
                "    }\n" +
                "```\n" +
                "\n" +
                "执行流程 ： \n" +
                "\n" +
                "1.后端接收请求后执行 UploadUtils.uploadPictureHandler(file)，【这是我自己封装的处理图片的工具，其实上就是SpringBoot处理文件上传的固定过程，不会的话可以搜索对应的博客】,我这里返回的是文件名在服务器中的名字。\n" +
                "\n" +
                "2.对处理结果进行判断，根据结果设置对应的json数据。\n" +
                "\n" +
                "3.返回json数据。\n" +
                "\n" +
                "\n" +
                "#### **4.效果图**\n" +
                "上传成功效果\n" +
                "![](/res/images/blogPicture/mushroom1647781677830.png)\n" +
                "\n" +
                "显示效果\n" +
                "![](/res/images/blogPicture/mushroom1647781862709.png)\n" +
                "\n" +
                "--------------------------------------------------\n" +
                "\n" +
                "#### **5.过程中出现的错误**\n" +
                "5.1 url不显示\n" +
                "可能是后端返回的json数据格式的问题，**注意：json数据的success属性为数字，不要写成字符串。**\n" +
                "```java\n" +
                "private int success; // 正确\n" +
                "private String success; //错误\n" +
                "```\n" +
                "\n" +
                "5.2 图片显示不出来\n" +
                "可能是图片在服务器中的相对地址写错了\n" +
                "例如：我的图片存储在 `/static/images`目录下，并且我在`application.yml`中配置了静态资源访问路径为 `/res`，那么我就应该返回此路径：\n" +
                "```java\n" +
                "/res/images/图片名.扩展名\n" +
                "```\n" +
                "\n" +
                "\n" +
                "5.3 editormd的上传文件框错位\n" +
                "打开F12查看元素，是因为那里的css样式被层叠了，只需要将加上 !important 就可以了。不用去修改editormd的css样式，只要在自定义css样式上写上对应的选择器就可以了。\n" +
                "```css\n" +
                ".editormd-form input[type=\"text\"] {\n" +
                "     display: inline-block;\n" +
                "     width: 264px !important;\n" +
                " }\n" +
                "```\n" +
                "\n" +
                "#### **6.结尾**\n" +
                "后端处理请求的代码不能直接完全复制去使用，可根据自己项目的实际情况做修改，但大体处理流程是一样的。\n";

        String text1 = "![](/res/images/blogPicture/mushroom1647781677dfsf830.png)";

        Boolean b= testStr.contains(text1);
        System.out.println(b);

//        Pattern pattern3 = Pattern.compile("\\!\\[.*\\]\\(.*\\)");
//        Pattern pattern3 = Pattern.compile("\\!\\[\\]\\((.*)+\\.(png|jpe?g|webp|gif|svg)\\)");
    }

}