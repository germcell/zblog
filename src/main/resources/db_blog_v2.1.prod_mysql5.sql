/*
 Navicat Premium Data Transfer

 Source Server         : root
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : localhost:3306
 Source Schema         : db_blog

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 19/09/2022 11:58:59
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_ad
-- ----------------------------
DROP TABLE IF EXISTS `tb_ad`;
CREATE TABLE `tb_ad`  (
  `aid` int(0) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `ad_code` char(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '活动代码，推荐为文章时为null',
  `type` tinyint(0) NOT NULL COMMENT '类型，1表示活动，2表示文章',
  `bid` int(0) NULL DEFAULT NULL COMMENT '类型为2时指定',
  `ad_title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型为1时指定活动标题',
  `ad_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型为1时指定活动描述',
  `ad_imgurl` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型为1时指定活动图片',
  `ad_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型为1时指定活动url',
  `create_time` timestamp(0) DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',
  `ad_status` tinyint(0) NOT NULL COMMENT '激活状态，0未激活、1激活；此表只能有一条记录处于激活状态',
  PRIMARY KEY (`aid`) USING BTREE,
  UNIQUE INDEX `ad_code`(`ad_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_ad
-- ----------------------------
INSERT INTO `tb_ad` VALUES (1, NULL, 2, 45, NULL, NULL, NULL, NULL, '2022-04-20 19:16:09', 0);
INSERT INTO `tb_ad` VALUES (2, 'ZB329', 1, NULL, '繁星计划(活动)', '第一人称为主，贴近生活，解析罪犯的犯罪动机和手段，寻觅真正的凶手，剧情设定合理，逻辑性强，把控人性。', '348f9e194f4062a17f.png', 'https://www.jianshu.com/activity?utm_medium=index-banner&utm_source=desktop', '2022-04-21 09:55:51', 1);

-- ----------------------------
-- Table structure for tb_blog
-- ----------------------------
DROP TABLE IF EXISTS `tb_blog`;
CREATE TABLE `tb_blog`  (
  `bid` int(0) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `first_picture` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `cid` int(0) NOT NULL,
  `views` int(0) NULL DEFAULT NULL,
  `is_appreciate` tinyint(1) NULL DEFAULT 1,
  `is_comment` tinyint(1) NULL DEFAULT 1,
  `is_reprint` tinyint(1) NULL DEFAULT NULL,
  `is_publish` tinyint(1) NULL DEFAULT NULL,
  `write_time` timestamp(0) DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp(0) DEFAULT CURRENT_TIMESTAMP,
  `uid` int(0) NOT NULL,
  `cr_tip_id` tinyint(0) NOT NULL COMMENT '版权归属',
  `like_num` int(0) NOT NULL DEFAULT 0 COMMENT '文章点赞量',
  PRIMARY KEY (`bid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 73 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_blog
-- ----------------------------
INSERT INTO `tb_blog` VALUES (41, 'tomcat修改端口、部署项目', '### 修改默认端口\r\n\r\n- 进入tomcat解压文件夹中依次进入 conf -> server.xml 找到里面的connector标签。\r\n\r\n\r\n\r\n### 部署工程\r\n\r\n1. 将工程文件夹复制到tomcat安装目录中的webapps文件夹中。\r\n\r\n2. 在 tomcat安装目录的 conf -> Catalina -> localhost 下新建xml文件。\r\n\r\n   ```xml\r\n   /*\r\n   	path:表示url访问路径\r\n   	docBase:表示工程文件夹(可为任意位置)\r\n   */\r\n   <Context path=\"/test2\" docBase=\"D:\\book\" />\r\n   ```', '23432423324.jpg', 1, 12, 1, 1, NULL, 1, '2022-03-06 15:38:12', '2022-06-25 19:28:20', 1, 4, 234);
INSERT INTO `tb_blog` VALUES (44, 'Stream流', '#### stream流\r\n\r\n1. 概念及作用\r\n\r\n   - 将数组或集合的元素全部取出，放在类似一条传送带上，我们可以在上面操作元素。主要作用是为了简化传统数组、集合的复杂的API操作，将操作简单化。\r\n\r\n     \r\n\r\n2. 获取stream流\r\n\r\n   - ```java\r\n     /*\r\n      *	1、集合通过stream()方法获取。\r\n      *	2、数组通过Arrays.stream()、stream.of()获取。\r\n      */\r\n     		// list集合获取流\r\n     		List<String> list = new ArrayList<>();\r\n     		Stream<String> streamList = list.stream();\r\n     		\r\n     		// map集合获取流\r\n     		Map<String, Integer> map = new HashMap<>();\r\n     		Stream<String> streamMap1 = map.keySet().stream();\r\n     		Stream<Integer> streamMap2 = map.values().stream();\r\n     		Stream<Entry<String, Integer>> stream = map.entrySet().stream();\r\n     	\r\n     		// 数组获取流\r\n     		int[] arr = {1, 2};\r\n     		String[] strarr = {\"sss\", \"fff\"};\r\n     		Stream<String> of3 = Stream.of(strarr);\r\n     		IntStream streamArr1 = Arrays.stream(arr);\r\n     		Stream<int[]> of = Stream.of(arr);\r\n     ```\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n', '23432423324.jpg', 1, 11, 0, 1, NULL, 1, '2022-03-10 17:33:20', '2022-06-25 17:47:23', 1, 4, 0);
INSERT INTO `tb_blog` VALUES (45, 'MyBatis配合PageHepler实现分页', '###分页插件使用\r\n\r\n#### 1.引入pagehelper依赖\r\n\r\n   ```xml-dtd\r\n   <!-- 分页插件   -->\r\n   <dependency>\r\n       <groupId>com.github.pagehelper</groupId>\r\n       <artifactId>pagehelper</artifactId>\r\n       <version>5.3.0</version>\r\n   </dependency>\r\n   ```\r\n\r\n#### 2.在MyBatis的配置文件中配置插件\r\n```xml-dtd\r\n<!--  配置分页插件  -->\r\n   <plugins>\r\n   	<plugin interceptor=\"com.github.pagehelper.PageInterceptor\"/>\r\n   </plugins>\r\n```\r\n\r\n#### 3.使用实例\r\n ```java\r\n   @org.junit.Test\r\n       public void testFindAll() {\r\n           UserDao dao = MyBatisUtils.getMapper(UserDao.class);\r\n           // 执行查询方法前设置分页（拦截器）\r\n           PageHelper.startPage(2,5);\r\n           // 执行查询所有记录的方法\r\n           List<User> allUser = dao.findAll();\r\n           // pageInfo为分页信息对象，包括总记录数、当前页、分页数据等信息\r\n           PageInfo<User> pageInfo = new PageInfo<User>(allUser);\r\n           // 遍历分页数据\r\n           for (User u : pageInfo.getList()) {\r\n               System.out.println(u);\r\n           }\r\n       }\r\n   ```\r\n\r\n#### 4.PageInfo对象(重要)\r\n>此对象包含了几乎所有的分页信息，如总页数、总记录数、上一页、下一页页码等等，在前端展示分页时能很方便的使用\r\n\r\n```java\r\npublic class PageInfo<T> extends PageSerializable<T> {\r\n       public static final int DEFAULT_NAVIGATE_PAGES = 8;\r\n       public static final PageInfo EMPTY = new PageInfo(Collections.emptyList(), 0);\r\n       private int pageNum;\r\n       private int pageSize;\r\n       private int size;\r\n       private long startRow;\r\n       private long endRow;\r\n       private int pages;\r\n       private int prePage;\r\n       private int nextPage;\r\n       private boolean isFirstPage;\r\n       private boolean isLastPage;\r\n       private boolean hasPreviousPage;\r\n       private boolean hasNextPage;\r\n       private int navigatePages;\r\n       private int[] navigatepageNums;\r\n       private int navigateFirstPage;\r\n       private int navigateLastPage;\r\n```\r\n\r\n-----------完\r\n', '23432423324.jpg', 27, 36, 1, 1, NULL, 1, '2022-03-11 09:17:28', '2022-06-03 12:00:00', 1, 1, 0);
INSERT INTO `tb_blog` VALUES (54, 'thymeleaf局部刷新表格后，js绑定事件失效', '#### 1、传统绑定事件方式\r\n```jquery\r\n$(\'.m-comment-pass\').on(\'click\', function() {\r\n\r\n})\r\n```\r\n>局部刷新后，会发现单击事件失效。\r\n因为局部刷新table表格后JQ识别不到元素，js事件不会触发，因为它已经替换了，因此，此时需要使用下面方式来进行事件绑定。\r\n\r\n\r\n#### 2、重新绑定事件\r\n```jquery\r\n$(document).on(\'click\', \'.m-comment-pass\', function () {\r\n\r\n})\r\n```', '23432423324.jpg', 33, 54, 1, 1, NULL, 1, '2022-03-18 16:59:19', '2022-06-03 12:00:00', 1, 1, 1);
INSERT INTO `tb_blog` VALUES (57, 'springboot+editormd上传图片功能', '#### **1、引言**\r\n>使用editormd + springboot实现md编辑器的图片上传功能\r\n\r\n#### **2、editormd部分**\r\n2.1通过js开启上传图片功能\r\n>imageFormats : 指定可接收的图片格式(我只列举了我工程所用到的)\r\n>imageUploadURL : 后端处理图片的请求\r\n\r\n```js\r\n imageUpload: true,\r\n imageFormats: [\"jpg\", \"jpeg\", \"png\"],\r\n imageUploadURL: \"http://127.0.0.1:8080/editormd/images\",\r\n onload: function () {\r\n console.log(\"begin upload\");\r\n }\r\n```\r\n#### **3、SpringBoot部分**\r\n3.1 json实体类封装\r\n>因为editormd上传图片后需要服务端返回一个json数据，来判断是否上传成功，并且类型、属性名必须与editormd规定的一致，也就是下述的实体类。\r\n\r\n```java\r\npublic class EditorJson {\r\n    /**\r\n     * editormd上传图片返回的json数据\r\n     */\r\n    private int success;  // 后端是否处理成功 1成功 0失败\r\n    private String message; // 提示信息\r\n    private String url; // 成功后的url地址（图片存储在服务器的地址，相对地址）\r\n}\r\n```\r\n\r\n3.2 请求处理\r\n>注意：请求参数设置为 editormd-image-file\r\n\r\n```java\r\n @PostMapping(\"/editormd/images\")\r\n @ResponseBody\r\n    public EditorJson imagesHandler(@RequestParam(\"editormd-image-file\") MultipartFile file) {\r\n        EditorJson result = new EditorJson();\r\n        try {\r\n            String accessDir = UploadUtils.uploadPictureHandler(file);\r\n            if (accessDir == null || Objects.equals(\"\", accessDir)) {\r\n                result.setSuccess(0);\r\n                result.setMessage(\"错误 : 只支持5MB的jpg,jpeg,png格式的图片\");\r\n            } else {\r\n                result.setSuccess(1);\r\n                result.setMessage(\"upload success\");\r\n                result.setUrl(\"/res/images/\" + accessDir);\r\n            }\r\n        } catch (Exception e) {\r\n            throw new UniversalException(\"图片上传失败,格式或大小错误\");\r\n        }\r\n        return result;\r\n    }\r\n```\r\n\r\n执行流程 ： \r\n\r\n1.后端接收请求后执行 UploadUtils.uploadPictureHandler(file)，【这是我自己封装的处理图片的工具，其实上就是SpringBoot处理文件上传的固定过程，不会的话可以搜索对应的博客】,我这里返回的是文件名在服务器中的名字。\r\n\r\n2.对处理结果进行判断，根据结果设置对应的json数据。\r\n\r\n3.返回json数据。\r\n\r\n\r\n#### **4.效果图**\r\n上传成功效果\r\n![](/res/images/blogPicture/mushroom1647781677830.png)\r\n\r\n显示效果\r\n![](/res/images/blogPicture/mushroom1647781862709.png)\r\n\r\n--------------------------------------------------\r\n\r\n#### **5.过程中出现的错误**\r\n5.1 url不显示\r\n可能是后端返回的json数据格式的问题，**注意：json数据的success属性为数字，不要写成字符串。**\r\n```java\r\nprivate int success; // 正确\r\nprivate String success; //错误\r\n```\r\n\r\n5.2 图片显示不出来\r\n可能是图片在服务器中的相对地址写错了\r\n例如：我的图片存储在 `/static/images`目录下，并且我在`application.yml`中配置了静态资源访问路径为 `/res`，那么我就应该返回此路径：\r\n```java\r\n/res/images/图片名.扩展名\r\n```\r\n\r\n\r\n5.3 editormd的上传文件框错位\r\n打开F12查看元素，是因为那里的css样式被层叠了，只需要将加上 !important 就可以了。不用去修改editormd的css样式，只要在自定义css样式上写上对应的选择器就可以了。\r\n```css\r\n.editormd-form input[type=\"text\"] {\r\n     display: inline-block;\r\n     width: 264px !important;\r\n }\r\n```\r\n\r\n#### **6.结尾**\r\n后端处理请求的代码不能直接完全复制去使用，可根据自己项目的实际情况做修改，但大体处理流程是一样的。\r\n', '23432423324.jpg', 32, 69, 1, 1, NULL, 1, '2022-03-20 21:04:07', '2022-06-03 12:00:00', 1, 1, 0);
INSERT INTO `tb_blog` VALUES (63, 'git pull错误：error: Your local changes to the following files would be overwritten by merge', '## 1.问题\r\n>项目存在多个分支，今天将某个分支的最新代码合并到master分支，然后pull报错\r\n\r\n## 2、解决\r\n```shell\r\ngit stash\r\ngit pull origin master\r\ngit stash pop\r\n```\r\n因为当前master分支的代码和远程仓库的不一样，如果强制合并，可能会照成本地代码被覆盖。所以先将本地代码存起来，再将远程仓库的代码pull下来，最后将然后再将最新的commit重新push到远程。', '23432423324.jpg', 35, 14, 1, 1, NULL, 1, '2022-03-28 12:57:08', '2022-06-03 12:00:00', 1, 1, 0);
INSERT INTO `tb_blog` VALUES (66, '测试1122222222', '测试测试车111111111', '34171651286274673.png', 34, 10923, 1, 1, 0, 1, '2022-04-25 18:53:07', '2022-06-03 12:00:00', 7, 1, 213);
INSERT INTO `tb_blog` VALUES (71, 'MyBatis配合PageHepler实现分页操作', '###分页插件使用\r\n\r\n#### 1.引入pagehelper依赖\r\n\r\n   ```xml-dtd\r\n   <!-- 分页插件   -->\r\n   <dependency>\r\n       <groupId>com.github.pagehelper</groupId>\r\n       <artifactId>pagehelper</artifactId>\r\n       <version>5.3.0</version>\r\n   </dependency>\r\n   ```\r\n\r\n#### 2.在MyBatis的配置文件中配置插件\r\n```xml-dtd\r\n<!--  配置分页插件  -->\r\n   <plugins>\r\n   	<plugin interceptor=\"com.github.pagehelper.PageInterceptor\"/>\r\n   </plugins>\r\n```\r\n\r\n#### 3.使用实例\r\n ```java\r\n   @org.junit.Test\r\n       public void testFindAll() {\r\n           UserDao dao = MyBatisUtils.getMapper(UserDao.class);\r\n           // 执行查询方法前设置分页（拦截器）\r\n           PageHelper.startPage(2,5);\r\n           // 执行查询所有记录的方法\r\n           List<User> allUser = dao.findAll();\r\n           // pageInfo为分页信息对象，包括总记录数、当前页、分页数据等信息\r\n           PageInfo<User> pageInfo = new PageInfo<User>(allUser);\r\n           // 遍历分页数据\r\n           for (User u : pageInfo.getList()) {\r\n               System.out.println(u);\r\n           }\r\n       }\r\n   ```\r\n\r\n#### 4.PageInfo对象(重要)\r\n>此对象包含了几乎所有的分页信息，如总页数、总记录数、上一页、下一页页码等等，在前端展示分页时能很方便的使用\r\n\r\n```java\r\npublic class PageInfo<T> extends PageSerializable<T> {\r\n       public static final int DEFAULT_NAVIGATE_PAGES = 8;\r\n       public static final PageInfo EMPTY = new PageInfo(Collections.emptyList(), 0);\r\n       private int pageNum;\r\n       private int pageSize;\r\n       private int size;\r\n       private long startRow;\r\n       private long endRow;\r\n       private int pages;\r\n       private int prePage;\r\n       private int nextPage;\r\n       private boolean isFirstPage;\r\n       private boolean isLastPage;\r\n       private boolean hasPreviousPage;\r\n       private boolean hasNextPage;\r\n       private int navigatePages;\r\n       private int[] navigatepageNums;\r\n       private int navigateFirstPage;\r\n       private int navigateLastPage;\r\n```\r\n\r\n-----------完-------------------\r\n', NULL, 27, 3234, 1, 1, 1, 1, '2022-04-29 21:25:59', '2022-06-03 12:00:00', 1, 4, 378);
INSERT INTO `tb_blog` VALUES (72, 'MyBatis配合PageHepler实现分页', '###分页插件使用\r\n\r\n#### 1.引入pagehelper依赖\r\n\r\n   ```xml-dtd\r\n   <!-- 分页插件   -->\r\n   <dependency>\r\n       <groupId>com.github.pagehelper</groupId>\r\n       <artifactId>pagehelper</artifactId>\r\n       <version>5.3.0</version>\r\n   </dependency>\r\n   ```\r\n\r\n#### 2.在MyBatis的配置文件中配置插件\r\n```xml-dtd\r\n<!--  配置分页插件  -->\r\n   <plugins>\r\n   	<plugin interceptor=\"com.github.pagehelper.PageInterceptor\"/>\r\n   </plugins>\r\n```\r\n\r\n#### 3.使用实例\r\n ```java\r\n   @org.junit.Test\r\n       public void testFindAll() {\r\n           UserDao dao = MyBatisUtils.getMapper(UserDao.class);\r\n           // 执行查询方法前设置分页（拦截器）\r\n           PageHelper.startPage(2,5);\r\n           // 执行查询所有记录的方法\r\n           List<User> allUser = dao.findAll();\r\n           // pageInfo为分页信息对象，包括总记录数、当前页、分页数据等信息\r\n           PageInfo<User> pageInfo = new PageInfo<User>(allUser);\r\n           // 遍历分页数据\r\n           for (User u : pageInfo.getList()) {\r\n               System.out.println(u);\r\n           }\r\n       }\r\n   ```\r\n\r\n#### 4.PageInfo对象(重要)\r\n>此对象包含了几乎所有的分页信息，如总页数、总记录数、上一页、下一页页码等等，在前端展示分页时能很方便的使用\r\n\r\n```java\r\npublic class PageInfo<T> extends PageSerializable<T> {\r\n       public static final int DEFAULT_NAVIGATE_PAGES = 8;\r\n       public static final PageInfo EMPTY = new PageInfo(Collections.emptyList(), 0);\r\n       private int pageNum;\r\n       private int pageSize;\r\n       private int size;\r\n       private long startRow;\r\n       private long endRow;\r\n       private int pages;\r\n       private int prePage;\r\n       private int nextPage;\r\n       private boolean isFirstPage;\r\n       private boolean isLastPage;\r\n       private boolean hasPreviousPage;\r\n       private boolean hasNextPage;\r\n       private int navigatePages;\r\n       private int[] navigatepageNums;\r\n       private int navigateFirstPage;\r\n       private int navigateLastPage;\r\n```\r\n\r\n-----------完\r\n', NULL, 32, 0, 1, 1, 1, 1, '2022-04-29 21:29:51', '2022-06-03 12:00:00', 1, 1, 0);
INSERT INTO `tb_blog` VALUES (73, 'MyBatis配合PageHepler实现分页', '###分页插件使用\r\n\r\n#### 1.引入pagehelper依赖\r\n\r\n   ```xml-dtd\r\n   <!-- 分页插件   -->\r\n   <dependency>\r\n       <groupId>com.github.pagehelper</groupId>\r\n       <artifactId>pagehelper</artifactId>\r\n       <version>5.3.0</version>\r\n   </dependency>\r\n   ```\r\n\r\n#### 2.在MyBatis的配置文件中配置插件\r\n```xml-dtd\r\n<!--  配置分页插件  -->\r\n   <plugins>\r\n   	<plugin interceptor=\"com.github.pagehelper.PageInterceptor\"/>\r\n   </plugins>\r\n```\r\n\r\n#### 3.使用实例\r\n ```java\r\n   @org.junit.Test\r\n       public void testFindAll() {\r\n           UserDao dao = MyBatisUtils.getMapper(UserDao.class);\r\n           // 执行查询方法前设置分页（拦截器）\r\n           PageHelper.startPage(2,5);\r\n           // 执行查询所有记录的方法\r\n           List<User> allUser = dao.findAll();\r\n           // pageInfo为分页信息对象，包括总记录数、当前页、分页数据等信息\r\n           PageInfo<User> pageInfo = new PageInfo<User>(allUser);\r\n           // 遍历分页数据\r\n           for (User u : pageInfo.getList()) {\r\n               System.out.println(u);\r\n           }\r\n       }\r\n   ```\r\n\r\n#### 4.PageInfo对象(重要)\r\n>此对象包含了几乎所有的分页信息，如总页数、总记录数、上一页、下一页页码等等，在前端展示分页时能很方便的使用\r\n\r\n```java\r\npublic class PageInfo<T> extends PageSerializable<T> {\r\n       public static final int DEFAULT_NAVIGATE_PAGES = 8;\r\n       public static final PageInfo EMPTY = new PageInfo(Collections.emptyList(), 0);\r\n       private int pageNum;\r\n       private int pageSize;\r\n       private int size;\r\n       private long startRow;\r\n       private long endRow;\r\n       private int pages;\r\n       private int prePage;\r\n       private int nextPage;\r\n       private boolean isFirstPage;\r\n       private boolean isLastPage;\r\n       private boolean hasPreviousPage;\r\n       private boolean hasNextPage;\r\n       private int navigatePages;\r\n       private int[] navigatepageNums;\r\n       private int navigateFirstPage;\r\n       private int navigateLastPage;\r\n```\r\n\r\n-----------完\r\n', NULL, 27, 0, 1, 1, 0, 1, '2022-04-29 21:35:04', '2022-06-03 12:00:00', 1, 1, 0);

-- ----------------------------
-- Table structure for tb_blog_outline
-- ----------------------------
DROP TABLE IF EXISTS `tb_blog_outline`;
CREATE TABLE `tb_blog_outline`  (
  `did` int(0) NOT NULL AUTO_INCREMENT COMMENT '于blog表bid保持一致',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '博客标题',
  `outline` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '博客的概要',
  `views` int(0) NOT NULL COMMENT '博客浏览数',
  `uid` int(0) NOT NULL COMMENT '用户id',
  `like_num` int(0) NULL DEFAULT 0 COMMENT '点赞量',
  `first_picture` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文章封面图',
  `write_time` timestamp(0) DEFAULT CURRENT_TIMESTAMP COMMENT '文章发布时间',
  `is_publish` tinyint(0) NULL DEFAULT NULL COMMENT '文章状态，同blog表一致',
  PRIMARY KEY (`did`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 74 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_blog_outline
-- ----------------------------
INSERT INTO `tb_blog_outline` VALUES (41, 'tomcat修改端口、部署项目', '修改默认端口进入tomcat解压文件夹中依次进入confserver.xml找到里面的connector标签部署工程1.将工程文件夹复制到tomcat安装目录中的webapps文件夹中2.在', 12, 1, 234, '23432423324.jpg', '2022-06-25 19:28:20', 1);
INSERT INTO `tb_blog_outline` VALUES (44, 'Stream流', 'stream流1.概念及作用将数组或集合的元素全部取出放在类似一条传送带上我们可以在上面操作元素主要作用是为了简化传统数组集合的复杂的操作将操作简单化2.获取stream流', 11, 1, 9, '23432423324.jpg', '2022-06-25 17:47:23', 1);
INSERT INTO `tb_blog_outline` VALUES (45, 'MyBatis配合PageHepler实现分页', '分页插件使用1.引入pagehelper依赖xmldtd!分页插件dependencygroupdcom.github.pagehelper/group', 36, 1, 0, '23432423324.jpg', '2022-04-21 17:18:42', 1);
INSERT INTO `tb_blog_outline` VALUES (54, 'thymeleaf局部刷新表格后，js绑定事件失效', '传统绑定事件方式jquery$.mcommentpass.onclick,function局部刷新后会发现单击事件失效因为局部刷新table表格后识别不到元素js事件不会触发', 54, 1, 1, '23432423324.jpg', '2022-05-20 17:29:01', 1);
INSERT INTO `tb_blog_outline` VALUES (57, 'springboot+editormd上传图片功能', '1引言使用editormd+springboot实现md编辑器的图片上传功能2editormd部分**通过js开启上传图片功能**imageormats指定可接收的图片格式我只列举了我工程所用到的im', 69, 1, 0, '23432423324.jpg', '2022-05-20 17:29:01', 1);
INSERT INTO `tb_blog_outline` VALUES (63, 'git pull错误：error: Your local changes to the following files would be overwritten by merge', '1.问题项目存在多个分支今天将某个分支的最新代码合并到master分支然后pull报错2解决shellgitstashgitpulloriginmastergitstashpop因为当前ma', 14, 1, 0, '23432423324.jpg', '2022-05-20 17:29:01', 1);
INSERT INTO `tb_blog_outline` VALUES (66, '测试1122222222', '测试测试车111111111', 10923, 7, 213, '34171651286274673.png', '2022-05-20 17:29:01', 1);
INSERT INTO `tb_blog_outline` VALUES (71, 'MyBatis配合PageHepler实现分页操作', '分页插件使用1.引入pagehelper依赖xmldtd!分页插件dependencygroupdcom.github.pagehelper/group', 3234, 1, 378, NULL, '2022-05-20 17:29:01', 1);
INSERT INTO `tb_blog_outline` VALUES (72, 'MyBatis配合PageHepler实现分页', '分页插件使用1.引入pagehelper依赖xmldtd!分页插件dependencygroupdcom.github.pagehelper/group', 0, 1, 0, NULL, '2022-04-29 21:29:51', 1);
INSERT INTO `tb_blog_outline` VALUES (73, 'MyBatis配合PageHepler实现分页', '分页插件使用1.引入pagehelper依赖xmldtd!分页插件dependencygroupdcom.github.pagehelper/group', 0, 1, 0, NULL, '2022-04-29 21:35:04', 1);

-- ----------------------------
-- Table structure for tb_category
-- ----------------------------
DROP TABLE IF EXISTS `tb_category`;
CREATE TABLE `tb_category`  (
  `cid` int(0) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `uid` int(0) NOT NULL,
  `create_time` timestamp(0) DEFAULT CURRENT_TIMESTAMP,
  `description` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `picture` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`cid`) USING BTREE,
  UNIQUE INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_category
-- ----------------------------
INSERT INTO `tb_category` VALUES (1, 'Java', 6, '2022-03-03 17:16:43', '一门以面向对象思想来编程的语言', '/res/images/category/java.jpg');
INSERT INTO `tb_category` VALUES (27, 'MyBatis', 6, '2022-02-27 22:22:28', '一个持久层框架', '/res/images/category/1645971748409.jpg');
INSERT INTO `tb_category` VALUES (32, 'SpringBoot', 6, '2022-03-18 16:49:43', '', '/res/images/category/1647593383975.png');
INSERT INTO `tb_category` VALUES (33, 'thymeleaf', 6, '2022-03-18 16:51:25', '', '/res/images/category/1647593485615.png');
INSERT INTO `tb_category` VALUES (34, '测试分类', 6, '2022-03-20 20:32:42', '', '/res/images/category/1647779562038.jpg');
INSERT INTO `tb_category` VALUES (35, 'Git', 6, '2022-03-28 12:49:31', '', '/res/images/category/1648442971724.jpg');

-- ----------------------------
-- Table structure for tb_comment
-- ----------------------------
DROP TABLE IF EXISTS `tb_comment`;
CREATE TABLE `tb_comment`  (
   `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '自增id、信息id',
   `send_id` bigint(0) NOT NULL COMMENT '发件人id',
   `send_avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发件人头像',
   `receive_id` bigint(0) NOT NULL COMMENT '收件人id',
   `content` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '内容',
   `receive_avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收件人头像',
   `p_id` bigint(0) NULL DEFAULT -1 COMMENT '文章评论的消息根id，固定为-1，置顶消息为0，评论由多个树组成，每一楼构成一棵树',
   `bid` int(0) NOT NULL DEFAULT -1 COMMENT '当msg_tag为1时的文章id',
   `reply_time` datetime(0) NULL DEFAULT NULL COMMENT '回复时间',
   `msg_tag` int(0) NOT NULL COMMENT '信件类型（0：私信 1：文章评论）',
   `is_read` tinyint(1) NOT NULL COMMENT '收件人是否已读（0：未读 1：已读）',
   `is_consume` tinyint(1) NULL DEFAULT NULL COMMENT '是否消费（0：未消费，1：已消费）',
   `create_time` datetime(0) NULL DEFAULT NULL,
   `update_time` datetime(0) NULL DEFAULT NULL,
   `send_visible` tinyint(1) NOT NULL DEFAULT 1 COMMENT '发送人是否可见（0：不可见，1：可见），删除消息时设置',
   `receive_visible` tinyint(1) NOT NULL DEFAULT 1 COMMENT '接收人是否可见（0：不可见，1：可见），查询时只有当这两个都为1才能显示',
   `exp` int(0) NULL DEFAULT NULL COMMENT '扩充字段',
   PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 62 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_comment
-- ----------------------------
INSERT INTO `tb_comment` VALUES (1, 1, NULL, 7, '你好 hello ~', NULL, -1, -1, '2022-09-25 23:44:45', 0, 1, 1, '2022-09-10 22:51:59', '2022-09-08 10:51:59', 1, 0, NULL);
INSERT INTO `tb_comment` VALUES (2, 1, NULL, 7, 'hello', NULL, -1, -1, '2022-09-25 23:44:45', 0, 1, 1, '2022-09-10 19:47:25', '2022-09-10 19:47:28', 1, 0, NULL);
INSERT INTO `tb_comment` VALUES (3, 8, NULL, 7, 'hello', NULL, -1, -1, '2022-09-28 14:03:40', 0, 1, 1, '2022-09-10 19:48:14', '2022-09-10 19:48:16', 1, 1, NULL);
INSERT INTO `tb_comment` VALUES (4, 8, NULL, 7, 'hello', NULL, -1, -1, '2022-09-28 14:03:40', 0, 1, 1, '2022-09-10 19:49:43', '2022-09-10 19:49:45', 1, 1, NULL);
INSERT INTO `tb_comment` VALUES (7, 7, NULL, 1, '怎么了', NULL, -1, -1, '2022-09-25 23:45:49', 0, 1, 1, '2022-09-10 20:00:09', '2022-09-11 20:48:13', 1, 1, NULL);
INSERT INTO `tb_comment` VALUES (20, 7, NULL, 2, 'wwwww', NULL, -1, -1, '2022-09-22 23:15:26', 0, 1, 1, '2022-09-22 19:43:20', '2022-09-22 19:43:20', 1, 1, NULL);
INSERT INTO `tb_comment` VALUES (21, 7, NULL, 2, 'qqqqq', NULL, -1, -1, '2022-09-22 23:15:26', 0, 1, 1, '2022-09-22 19:52:50', '2022-09-22 19:52:50', 1, 1, NULL);
INSERT INTO `tb_comment` VALUES (23, 7, NULL, 2, 'ffff', NULL, -1, -1, '2022-09-22 23:15:26', 0, 1, 1, '2022-09-22 19:53:37', '2022-09-22 19:53:37', 1, 1, NULL);
INSERT INTO `tb_comment` VALUES (24, 7, NULL, 2, '11111', NULL, -1, -1, '2022-09-22 23:15:26', 0, 1, 1, '2022-09-22 20:00:24', '2022-09-22 20:00:24', 1, 1, NULL);
INSERT INTO `tb_comment` VALUES (25, 7, NULL, 2, 'how are u?', NULL, -1, -1, '2022-09-22 23:15:26', 0, 1, 1, '2022-09-22 20:56:18', '2022-09-22 20:56:18', 1, 1, NULL);
INSERT INTO `tb_comment` VALUES (26, 7, NULL, 2, 'nice to meet to', NULL, -1, -1, '2022-09-22 23:15:26', 0, 1, 1, '2022-09-22 20:56:28', '2022-09-22 20:56:28', 1, 1, NULL);
INSERT INTO `tb_comment` VALUES (28, 1, NULL, 7, 'fuck u', NULL, -1, -1, '2022-09-25 23:44:45', 0, 1, 1, '2022-09-22 20:58:17', '2022-09-22 20:58:17', 1, 0, NULL);
INSERT INTO `tb_comment` VALUES (29, 7, NULL, 2, '1111111', NULL, -1, -1, '2022-09-22 23:15:26', 0, 1, 1, '2022-09-22 22:20:34', '2022-09-22 22:20:34', 1, 1, NULL);
INSERT INTO `tb_comment` VALUES (30, 2, NULL, 7, '发射点发射点', NULL, -1, -1, '2022-09-28 14:05:27', 0, 1, 1, '2022-09-22 22:20:45', '2022-09-22 22:20:45', 1, 1, NULL);
INSERT INTO `tb_comment` VALUES (31, 7, NULL, 2, 'hhh', NULL, -1, -1, '2022-09-22 23:15:26', 0, 1, 1, '2022-09-22 22:21:54', '2022-09-22 22:21:54', 1, 1, NULL);
INSERT INTO `tb_comment` VALUES (32, 7, NULL, 2, '11', NULL, -1, -1, '2022-09-22 23:15:26', 0, 1, 1, '2022-09-22 22:26:08', '2022-09-22 22:26:08', 1, 1, NULL);
INSERT INTO `tb_comment` VALUES (33, 7, NULL, 2, '11111', NULL, -1, -1, '2022-09-22 23:15:26', 0, 1, 1, '2022-09-22 23:11:27', '2022-09-22 23:11:27', 1, 1, NULL);
INSERT INTO `tb_comment` VALUES (34, 7, NULL, 8, 'nihao\n', NULL, -1, -1, NULL, 0, 0, 1, '2022-09-23 15:38:23', '2022-09-23 15:38:23', 1, 1, NULL);
INSERT INTO `tb_comment` VALUES (35, 7, NULL, 1, 'ggggg', NULL, -1, -1, '2022-09-25 23:45:49', 0, 1, 1, '2022-09-23 15:38:23', '2022-09-23 15:38:23', 1, 1, NULL);
INSERT INTO `tb_comment` VALUES (36, 7, NULL, 1, 'ggggg', NULL, -1, -1, '2022-09-25 23:45:49', 0, 1, 1, '2022-09-23 15:38:25', '2022-09-23 15:38:25', 1, 1, NULL);
INSERT INTO `tb_comment` VALUES (37, 7, NULL, 1, 'uuuu', NULL, -1, -1, '2022-09-25 23:45:49', 0, 1, 1, '2022-09-23 15:38:55', '2022-09-23 15:38:55', 1, 1, NULL);
INSERT INTO `tb_comment` VALUES (38, 1, NULL, 7, '脚后跟经过', NULL, -1, -1, '2022-09-25 23:44:45', 0, 1, 1, '2022-09-23 15:39:00', '2022-09-23 15:39:00', 1, 0, NULL);
INSERT INTO `tb_comment` VALUES (39, 7, NULL, 1, '999', NULL, -1, -1, '2022-09-25 23:45:49', 0, 1, 1, '2022-09-23 15:39:16', '2022-09-23 15:39:16', 1, 1, NULL);
INSERT INTO `tb_comment` VALUES (40, 7, NULL, 2, '好吧好吧\n', NULL, -1, -1, NULL, 0, 0, 1, '2022-09-26 09:39:27', '2022-09-26 09:39:27', 1, 1, NULL);
INSERT INTO `tb_comment` VALUES (41, 7, NULL, 2, 'dsf\nsdf\n', NULL, -1, -1, NULL, 0, 0, 1, '2022-09-26 09:53:51', '2022-09-26 09:53:51', 1, 1, NULL);
INSERT INTO `tb_comment` VALUES (42, 7, NULL, 1, '不错', NULL, -1, 41, NULL, 1, 0, 1, '2022-09-26 12:14:28', '2022-09-26 12:14:31', 1, 1, NULL);
INSERT INTO `tb_comment` VALUES (43, 8, NULL, 7, '嗯嗯', NULL, 42, 41, '2022-09-28 14:03:40', 1, 1, 1, '2022-09-26 12:48:43', '2022-09-26 12:48:46', 1, 1, NULL);
INSERT INTO `tb_comment` VALUES (44, 8, NULL, 1, '还可以', NULL, -1, 41, NULL, 1, 0, 1, '2022-09-26 12:50:33', '2022-09-26 12:50:36', 1, 1, NULL);
INSERT INTO `tb_comment` VALUES (45, 10, NULL, 8, '谢谢', NULL, 44, 41, NULL, 1, 0, 1, '2022-09-26 12:51:43', '2022-09-26 12:51:46', 1, 1, NULL);
INSERT INTO `tb_comment` VALUES (46, 1, NULL, 10, '嗯嗯嗯', NULL, 45, 41, NULL, 1, 0, 1, '2022-09-26 12:57:06', '2022-09-26 12:57:10', 1, 1, NULL);
INSERT INTO `tb_comment` VALUES (47, 9, NULL, 1, '还不错噢', NULL, -1, 41, NULL, 1, 0, 1, '2022-09-26 13:35:41', '2022-09-26 13:35:44', 1, 1, NULL);
INSERT INTO `tb_comment` VALUES (48, 10, '', 8, '有道理', NULL, 43, 41, NULL, 1, 0, 1, '2022-09-26 17:26:33', '2022-09-26 17:26:35', 1, 1, NULL);
INSERT INTO `tb_comment` VALUES (49, 9, NULL, 8, '我试试', NULL, 43, 41, NULL, 1, 0, 1, '2022-09-27 09:17:40', '2022-09-27 09:17:43', 1, 1, NULL);
INSERT INTO `tb_comment` VALUES (50, 7, NULL, 1, '发士大夫', NULL, -1, 41, NULL, 1, 0, 1, '2022-09-27 19:16:41', '2022-09-27 19:16:41', 1, 1, NULL);
INSERT INTO `tb_comment` VALUES (51, 7, NULL, 1, '方法', NULL, -1, 41, NULL, 1, 0, 1, '2022-09-27 19:18:04', '2022-09-27 19:18:04', 1, 1, NULL);
INSERT INTO `tb_comment` VALUES (52, 7, NULL, 1, '烦烦烦烦烦烦', NULL, -1, 41, NULL, 1, 0, 1, '2022-09-27 19:18:39', '2022-09-27 19:18:39', 1, 1, NULL);
INSERT INTO `tb_comment` VALUES (53, 7, NULL, 1, '呱呱呱呱呱呱', NULL, -1, 41, NULL, 1, 0, 1, '2022-09-27 19:20:54', '2022-09-27 19:20:54', 1, 1, NULL);
INSERT INTO `tb_comment` VALUES (54, 7, NULL, 1, '哈哈哈哈哈', NULL, -1, 41, NULL, 1, 0, 1, '2022-09-27 19:24:20', '2022-09-27 19:24:20', 1, 1, NULL);
INSERT INTO `tb_comment` VALUES (55, 7, NULL, 1, '哈克', NULL, -1, 41, NULL, 1, 0, 1, '2022-09-27 19:33:47', '2022-09-27 19:33:47', 1, 1, NULL);
INSERT INTO `tb_comment` VALUES (56, 7, NULL, 1, '烦烦烦方法', NULL, -1, 41, NULL, 1, 0, 1, '2022-09-27 19:34:42', '2022-09-27 19:34:42', 1, 1, NULL);
INSERT INTO `tb_comment` VALUES (57, 7, NULL, 1, '怎么了', NULL, 56, 41, NULL, 1, 0, 1, '2022-09-27 22:01:15', '2022-09-27 22:01:15', 1, 1, NULL);
INSERT INTO `tb_comment` VALUES (58, 7, NULL, 1, '000000', NULL, -1, 41, NULL, 1, 0, 1, '2022-09-27 22:10:28', '2022-09-27 22:10:28', 1, 1, NULL);
INSERT INTO `tb_comment` VALUES (59, 7, NULL, 7, '你好棒', NULL, 58, 41, NULL, 1, 0, 1, '2022-09-27 22:10:40', '2022-09-27 22:10:40', 1, 1, NULL);
INSERT INTO `tb_comment` VALUES (60, 7, NULL, 1, '烦烦烦烦烦烦', NULL, -1, 41, NULL, 1, 0, 1, '2022-09-28 00:07:37', '2022-09-28 00:07:37', 1, 1, NULL);
INSERT INTO `tb_comment` VALUES (61, 7, NULL, 7, '呢嫩嫩\n', NULL, 55, 41, NULL, 1, 0, 1, '2022-09-28 00:10:23', '2022-09-28 00:10:23', 1, 1, NULL);

-- ----------------------------
-- Records of tb_comment
-- ----------------------------
INSERT INTO `tb_comment` VALUES (1, 1, NULL, 7, '你好 hello ~', NULL, -1, -1, '2022-09-14 22:16:04', 0, 1, 1, '2022-09-10 22:51:59', '2022-09-08 10:51:59');
INSERT INTO `tb_comment` VALUES (2, 1, NULL, 7, 'hello', NULL, -1, -1, '2022-09-14 22:16:04', 0, 1, 1, '2022-09-10 19:47:25', '2022-09-10 19:47:28');
INSERT INTO `tb_comment` VALUES (3, 8, NULL, 7, 'hello', NULL, -1, -1, '2022-09-14 20:58:24', 0, 1, 1, '2022-09-10 19:48:14', '2022-09-10 19:48:16');
INSERT INTO `tb_comment` VALUES (4, 8, NULL, 7, 'hello', NULL, -1, -1, '2022-09-14 20:58:24', 0, 1, 1, '2022-09-10 19:49:43', '2022-09-10 19:49:45');
INSERT INTO `tb_comment` VALUES (5, 9, NULL, 7, 'hello', NULL, -1, -1, '2022-09-14 21:47:47', 0, 1, 1, '2022-09-10 19:51:05', '2022-09-10 19:51:09');
INSERT INTO `tb_comment` VALUES (6, 9, NULL, 7, 'hello', NULL, -1, -1, '2022-09-14 21:47:47', 0, 1, 1, '2022-09-10 19:52:26', '2022-09-10 19:52:29');
INSERT INTO `tb_comment` VALUES (7, 7, NULL, 1, '怎么了', NULL, -1, -1, '2022-09-14 20:01:11', 0, 0, 1, '2022-09-10 20:00:09', '2022-09-11 20:48:13');

-- ----------------------------
-- Table structure for tb_copyright
-- ----------------------------
DROP TABLE IF EXISTS `tb_copyright`;
CREATE TABLE `tb_copyright`  (
  `cr_id` int(0) NOT NULL AUTO_INCREMENT,
  `cr_tip` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `cr_tip_id` int(0) NOT NULL COMMENT '版权标识',
  PRIMARY KEY (`cr_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_copyright
-- ----------------------------
INSERT INTO `tb_copyright` VALUES (1, '原创', 1);
INSERT INTO `tb_copyright` VALUES (2, '转载', 2);
INSERT INTO `tb_copyright` VALUES (3, '翻译', 3);
INSERT INTO `tb_copyright` VALUES (4, '原+转', 4);

-- ----------------------------
-- Table structure for tb_fans
-- ----------------------------
DROP TABLE IF EXISTS `tb_fans`;
CREATE TABLE `tb_fans`  (
  `fid` int(0) NOT NULL AUTO_INCREMENT COMMENT '自增id主键',
  `uid` int(0) NOT NULL COMMENT '被关注人id',
  `uid2` int(0) NOT NULL COMMENT '主关注人id',
  `join_time` timestamp(0) DEFAULT CURRENT_TIMESTAMP COMMENT '关注时间',
  PRIMARY KEY (`fid`) USING BTREE,
  UNIQUE INDEX `uid_uid2`(`uid`, `uid2`) USING BTREE COMMENT '唯一索引'
) ENGINE = InnoDB AUTO_INCREMENT = 75 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_fans
-- ----------------------------
INSERT INTO `tb_fans` VALUES (68, 2, 7, '2022-05-19 10:19:29');
INSERT INTO `tb_fans` VALUES (73, 8, 7, '2022-05-19 10:26:01');
INSERT INTO `tb_fans` VALUES (74, 1, 2, '2022-05-19 10:26:01');
INSERT INTO `tb_fans` VALUES (75, 8, 2, '2022-05-19 10:26:01');

-- ----------------------------
-- Table structure for tb_like
-- ----------------------------
DROP TABLE IF EXISTS `tb_like`;
CREATE TABLE `tb_like`  (
  `lid` int(0) NOT NULL AUTO_INCREMENT COMMENT '自增id主键',
  `bid` int(0) NOT NULL COMMENT '被点赞文章id',
  `uid` int(0) NOT NULL COMMENT '被点赞文章作者id',
  `mid` int(0) NOT NULL COMMENT '点赞人用户id',
  `like_time` timestamp(0)  DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
  PRIMARY KEY (`lid`) USING BTREE,
  UNIQUE INDEX `bid_uid_mid`(`bid`, `uid`, `mid`) USING BTREE COMMENT 'tb_like表的唯一索引'
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_like
-- ----------------------------
INSERT INTO `tb_like` VALUES (1, 41, 1, 7, '2022-05-12 22:29:30');
INSERT INTO `tb_like` VALUES (2, 44, 1, 7, '2022-05-12 22:29:38');
INSERT INTO `tb_like` VALUES (3, 54, 1, 7, '2022-05-12 22:29:46');

-- ----------------------------
-- Table structure for tb_order
-- ----------------------------
DROP TABLE IF EXISTS `tb_order`;
CREATE TABLE `tb_order`  (
  `oid` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `order_no` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单号',
  `money` decimal(10, 2) NOT NULL COMMENT '订单金额',
  `subject` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '支付主题，描述',
  `uid` int(0) NOT NULL COMMENT '打赏用户id',
  `uid2` int(0) NOT NULL COMMENT '被打赏用户id',
  `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单状态：2订单超时(20min)，1已支付，0待支付',
  `comment` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '打赏留言',
  `create_time` timestamp(0)  default CURRENT_TIMESTAMP COMMENT '订单生成时间',
  `alipay_trade_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付宝支付流水号',
  `pay_time` timestamp(0)  DEFAULT CURRENT_TIMESTAMP COMMENT '用户支付成功时间',
  PRIMARY KEY (`oid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_order
-- ----------------------------
INSERT INTO `tb_order` VALUES (1, '202206051130071654399807644', 199.00, '赞赏undefined', 7, 1, '0', '', '2022-06-05 11:30:08', NULL, NULL);
INSERT INTO `tb_order` VALUES (2, '202206051136591654400219357', 188.00, '赞赏小绵羊', 7, 1, '0', '你写的很好，希望继续努力', '2022-06-05 11:36:59', NULL, NULL);
INSERT INTO `tb_order` VALUES (3, '202206051138091654400289382', 111.00, '赞赏小绵羊', 7, 1, '0', '', '2022-06-05 11:38:09', NULL, NULL);
INSERT INTO `tb_order` VALUES (4, '202206051143031654400583706', 1.00, '赞赏小绵羊', 7, 1, '0', '', '2022-06-05 11:43:04', NULL, NULL);
INSERT INTO `tb_order` VALUES (5, '202206051144461654400686802', 1.00, '赞赏小绵羊', 7, 1, '0', '', '2022-06-05 11:44:47', NULL, NULL);
INSERT INTO `tb_order` VALUES (6, '202206051147411654400861075', 1.00, '赞赏小绵羊', 7, 1, '0', '', '2022-06-05 11:47:41', NULL, NULL);
INSERT INTO `tb_order` VALUES (7, '202206051152381654401158009', 1.00, '赞赏小绵羊', 7, 1, '0', '', '2022-06-05 11:52:38', NULL, NULL);
INSERT INTO `tb_order` VALUES (8, '202206051152591654401179869', 1.00, '赞赏小绵羊', 7, 1, '0', '', '2022-06-05 11:53:00', NULL, NULL);
INSERT INTO `tb_order` VALUES (9, '202206051154041654401244883', 20.00, '赞赏小绵羊', 7, 1, '0', '', '2022-06-05 11:54:05', NULL, NULL);
INSERT INTO `tb_order` VALUES (10, '202206051209241654402164019', 20.00, '赞赏小绵羊', 7, 1, '0', '', '2022-06-05 12:09:24', NULL, NULL);
INSERT INTO `tb_order` VALUES (11, '202206051319341654406374934', 20.00, '赞赏小绵羊', 7, 1, '0', NULL, '2022-06-05 13:19:35', NULL, NULL);
INSERT INTO `tb_order` VALUES (12, '202206051320491654406449023', 209.00, '赞赏小绵羊', 7, 1, '0', NULL, '2022-06-05 13:20:49', NULL, NULL);
INSERT INTO `tb_order` VALUES (13, '202206051323051654406585670', 209.00, '赞赏小绵羊', 7, 1, '0', NULL, '2022-06-05 13:23:06', NULL, NULL);
INSERT INTO `tb_order` VALUES (14, '202206051331281654407088589', 6.00, '赞赏小绵羊', 7, 1, '0', NULL, '2022-06-05 13:31:29', NULL, NULL);
INSERT INTO `tb_order` VALUES (15, '202206051333221654407202115', 6.00, '赞赏小绵羊', 7, 1, '0', NULL, '2022-06-05 13:33:22', NULL, NULL);
INSERT INTO `tb_order` VALUES (16, '202206051334411654407281878', 20.00, '赞赏小绵羊', 7, 1, '0', NULL, '2022-06-05 13:34:42', NULL, NULL);
INSERT INTO `tb_order` VALUES (17, '202206051350301654408230650', 20.00, '赞赏小绵羊', 7, 1, '0', NULL, '2022-06-05 13:50:31', NULL, NULL);
INSERT INTO `tb_order` VALUES (18, '202206221527471655882867025', 20.00, '赞赏冰墩墩', 7, 7, '0', NULL, '2022-06-22 15:27:47', NULL, NULL);
INSERT INTO `tb_order` VALUES (19, '202206221531391655883099099', 20.00, '赞赏冰墩墩', 7, 7, '0', NULL, '2022-06-22 15:31:39', NULL, NULL);
INSERT INTO `tb_order` VALUES (20, '202206221533161655883196846', 20.00, '赞赏冰墩墩', 7, 7, '0', NULL, '2022-06-22 15:33:17', NULL, NULL);
INSERT INTO `tb_order` VALUES (21, '202206221533541655883234189', 4.00, '赞赏冰墩墩', 7, 7, '0', NULL, '2022-06-22 15:33:54', NULL, NULL);
INSERT INTO `tb_order` VALUES (22, '202206221546081655883968372', 20.00, '赞赏冰墩墩', 7, 7, '0', NULL, '2022-06-22 15:46:09', NULL, NULL);
INSERT INTO `tb_order` VALUES (23, '202206221611321655885492380', 20.00, '赞赏小绵羊', 7, 1, '1', NULL, '2022-06-22 16:12:26', '2022062222001454190501431795', '2022-06-22 16:12:19');
INSERT INTO `tb_order` VALUES (24, '202206221618141655885894585', 20.00, '赞赏小绵羊', 7, 1, '0', '', '2022-06-22 16:18:15', NULL, NULL);
INSERT INTO `tb_order` VALUES (25, '202206221618571655885937055', 20.00, '赞赏小绵羊', 7, 1, '0', '你好发多少', '2022-06-22 16:18:57', NULL, NULL);
INSERT INTO `tb_order` VALUES (26, '202206251725281656149128154', 20.00, '赞赏小绵羊', 7, 1, '0', ' 你好', '2022-06-25 17:25:28', NULL, NULL);
INSERT INTO `tb_order` VALUES (27, '202206251739491656149989592', 20.00, '赞赏小绵羊', 7, 1, '0', '哈哈哈', '2022-06-25 17:39:50', NULL, NULL);

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `uid` int(0) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(18) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `pwd` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `mail` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `type` int(0) NOT NULL,
  `register_time` timestamp(0)  DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp(0)  DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`uid`) USING BTREE,
  UNIQUE INDEX `mail`(`mail`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES (1, 'jackZ', '9b04d152845ec0a378394003c96da594', 'jack@qq.com', NULL, 0, '2022-02-22 16:18:14', '2022-04-05 21:54:03');
INSERT INTO `tb_user` VALUES (6, 'mushroom', '698d51a19d8a121ce581499d7b701668', '2899268091@qq.com', '/res/images/del_avatar3.jpeg', 0, '2022-02-23 23:15:57', '2022-04-20 20:39:52');

-- ----------------------------
-- Table structure for tb_writer
-- ----------------------------
DROP TABLE IF EXISTS `tb_writer`;
CREATE TABLE `tb_writer`  (
  `uid` int(0) NOT NULL AUTO_INCREMENT COMMENT '用户id，主键，自增长',
  `writer_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名、笔名',
  `writer_sex` char(6) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'male' COMMENT '性别',
  `writer_age` int(0) NULL DEFAULT NULL COMMENT '年龄',
  `writer_birthday` timestamp(0) NULL DEFAULT NULL COMMENT '出生日期',
  `mail` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登录邮箱',
  `pwd` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `writer_status` int(0) NOT NULL DEFAULT 1 COMMENT '用户状态:1、正常 0、封禁',
  `writer_avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户头像',
  `writer_phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户电话，只支持<=11位号码',
  `writer_introduce` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '个人介绍',
  `register_time` timestamp(0) default CURRENT_TIMESTAMP COMMENT '注册时间',
  `update_time` timestamp(0)  DEFAULT CURRENT_TIMESTAMP COMMENT '信息更新时间',
  `is_member` int(0) NULL DEFAULT NULL COMMENT '扩展字段',
  `fans` int(0) NULL DEFAULT 0 COMMENT '用户粉丝数',
  `article_num` int(0) NOT NULL DEFAULT 0 COMMENT '发表文章数量（除开草稿状态）',
  `all_views` bigint(0) NULL DEFAULT 0 COMMENT '用户发布的所有文章的浏览量',
  `all_like_nums` bigint(0) NULL DEFAULT 0 COMMENT '用户所有文章点赞数',
  PRIMARY KEY (`uid`) USING BTREE,
  UNIQUE INDEX `writer_name`(`writer_name`) USING BTREE,
  UNIQUE INDEX `mail`(`mail`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_writer
-- ----------------------------
INSERT INTO `tb_writer` VALUES (1, '小绵羊', 'male', NULL, NULL, '44149734@qq.com', '698d51a19d8a121ce581499d7b701668', 1, '1231243dfs.jpg', NULL, '哈哈哈哈哈哈哈哈哈哈哈', '2022-04-20 19:15:16', '2022-04-20 19:15:20', NULL, 121, 9, 3422, 0);
INSERT INTO `tb_writer` VALUES (2, '牛爱花', 'female', NULL, NULL, '213@qq.com', '111', 1, 'common_avatar.png', NULL, NULL, '2022-04-21 11:57:57', '2022-04-21 11:58:00', NULL, 0, 6, 0, 0);
INSERT INTO `tb_writer` VALUES (7, '冰墩墩', 'female', NULL, '2022-02-01 08:00:00', '441497343@qq.com', '81dc9bdb52d04dc20036dbd8313ed055', 1, '441497343@qq.com1652757082541.png', '17623147890', '墩墩墩墩墩墩墩墩墩墩墩墩', '2022-04-23 10:05:03', '2022-05-17 11:11:23', NULL, 324, 10, 12267, 2564);
INSERT INTO `tb_writer` VALUES (8, '11111', 'male', NULL, NULL, '2899268091@qq.com', '698d51a19d8a121ce581499d7b701668', 1, 'common_avatar.png', NULL, NULL, '2022-04-23 15:23:41', NULL, NULL, 0, 34, 0, 0);
INSERT INTO `tb_writer` VALUES (9, '日月同错', 'male', NULL, NULL, '2855461512@qq.com', 'b0329dd8565a9853710eab2eea8c9f5e', 1, '2855461512@qq.com1652956583620.jpg', NULL, NULL, '2022-05-19 16:56:16', '2022-05-19 18:36:24', NULL, 0, 0, 0, 0);

SET FOREIGN_KEY_CHECKS = 1;
