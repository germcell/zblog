/*
Navicat MySQL Data Transfer

Source Server         : root
Source Server Version : 80028
Source Host           : localhost:3306
Source Database       : db_blog

Target Server Type    : MYSQL
Target Server Version : 80028
File Encoding         : 65001

Date: 2022-03-28 12:28:12
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_blog
-- ----------------------------
DROP TABLE IF EXISTS `tb_blog`;
CREATE TABLE `tb_blog` (
  `bid` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `first_picture` varchar(255) DEFAULT NULL,
  `cid` int NOT NULL,
  `views` int DEFAULT NULL,
  `is_appreciate` tinyint(1) DEFAULT '1',
  `is_comment` tinyint(1) DEFAULT '1',
  `is_reprint` tinyint(1) DEFAULT NULL,
  `is_publish` tinyint(1) DEFAULT NULL,
  `write_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `uid` int NOT NULL,
  `cr_tip_id` tinyint NOT NULL COMMENT '版权归属',
  PRIMARY KEY (`bid`)
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of tb_blog
-- ----------------------------
INSERT INTO `tb_blog` VALUES ('41', 'tomcat修改端口、部署项目', '### 修改默认端口\r\n\r\n- 进入tomcat解压文件夹中依次进入 conf -> server.xml 找到里面的connector标签。\r\n\r\n\r\n\r\n### 部署工程\r\n\r\n1. 将工程文件夹复制到tomcat安装目录中的webapps文件夹中。\r\n\r\n2. 在 tomcat安装目录的 conf -> Catalina -> localhost 下新建xml文件。\r\n\r\n   ```xml\r\n   /*\r\n   	path:表示url访问路径\r\n   	docBase:表示工程文件夹(可为任意位置)\r\n   */\r\n   <Context path=\"/test2\" docBase=\"D:\\book\" />\r\n   ```', null, '1', '12', '1', '1', null, '1', '2022-03-06 15:38:12', '2022-03-22 16:40:23', '6', '4');
INSERT INTO `tb_blog` VALUES ('44', 'Stream流', '#### stream流\r\n\r\n1. 概念及作用\r\n\r\n   - 将数组或集合的元素全部取出，放在类似一条传送带上，我们可以在上面操作元素。主要作用是为了简化传统数组、集合的复杂的API操作，将操作简单化。\r\n\r\n     \r\n\r\n2. 获取stream流\r\n\r\n   - ```java\r\n     /*\r\n      *	1、集合通过stream()方法获取。\r\n      *	2、数组通过Arrays.stream()、stream.of()获取。\r\n      */\r\n     		// list集合获取流\r\n     		List<String> list = new ArrayList<>();\r\n     		Stream<String> streamList = list.stream();\r\n     		\r\n     		// map集合获取流\r\n     		Map<String, Integer> map = new HashMap<>();\r\n     		Stream<String> streamMap1 = map.keySet().stream();\r\n     		Stream<Integer> streamMap2 = map.values().stream();\r\n     		Stream<Entry<String, Integer>> stream = map.entrySet().stream();\r\n     	\r\n     		// 数组获取流\r\n     		int[] arr = {1, 2};\r\n     		String[] strarr = {\"sss\", \"fff\"};\r\n     		Stream<String> of3 = Stream.of(strarr);\r\n     		IntStream streamArr1 = Arrays.stream(arr);\r\n     		Stream<int[]> of = Stream.of(arr);\r\n     ```\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n', null, '1', '11', '0', '1', null, '1', '2022-03-10 17:33:20', '2022-03-22 16:40:23', '6', '4');
INSERT INTO `tb_blog` VALUES ('45', 'MyBatis配合PageHepler实现分页', '###分页插件使用\r\n\r\n#### 1.引入pagehelper依赖\r\n\r\n   ```xml-dtd\r\n   <!-- 分页插件   -->\r\n   <dependency>\r\n       <groupId>com.github.pagehelper</groupId>\r\n       <artifactId>pagehelper</artifactId>\r\n       <version>5.3.0</version>\r\n   </dependency>\r\n   ```\r\n\r\n#### 2.在MyBatis的配置文件中配置插件\r\n```xml-dtd\r\n<!--  配置分页插件  -->\r\n   <plugins>\r\n   	<plugin interceptor=\"com.github.pagehelper.PageInterceptor\"/>\r\n   </plugins>\r\n```\r\n\r\n#### 3.使用实例\r\n ```java\r\n   @org.junit.Test\r\n       public void testFindAll() {\r\n           UserDao dao = MyBatisUtils.getMapper(UserDao.class);\r\n           // 执行查询方法前设置分页（拦截器）\r\n           PageHelper.startPage(2,5);\r\n           // 执行查询所有记录的方法\r\n           List<User> allUser = dao.findAll();\r\n           // pageInfo为分页信息对象，包括总记录数、当前页、分页数据等信息\r\n           PageInfo<User> pageInfo = new PageInfo<User>(allUser);\r\n           // 遍历分页数据\r\n           for (User u : pageInfo.getList()) {\r\n               System.out.println(u);\r\n           }\r\n       }\r\n   ```\r\n\r\n#### 4.PageInfo对象(重要)\r\n>此对象包含了几乎所有的分页信息，如总页数、总记录数、上一页、下一页页码等等，在前端展示分页时能很方便的使用\r\n\r\n```java\r\npublic class PageInfo<T> extends PageSerializable<T> {\r\n       public static final int DEFAULT_NAVIGATE_PAGES = 8;\r\n       public static final PageInfo EMPTY = new PageInfo(Collections.emptyList(), 0);\r\n       private int pageNum;\r\n       private int pageSize;\r\n       private int size;\r\n       private long startRow;\r\n       private long endRow;\r\n       private int pages;\r\n       private int prePage;\r\n       private int nextPage;\r\n       private boolean isFirstPage;\r\n       private boolean isLastPage;\r\n       private boolean hasPreviousPage;\r\n       private boolean hasNextPage;\r\n       private int navigatePages;\r\n       private int[] navigatepageNums;\r\n       private int navigateFirstPage;\r\n       private int navigateLastPage;\r\n```\r\n\r\n-----------完\r\n', null, '27', '6', '1', '1', null, '1', '2022-03-11 09:17:28', '2022-03-23 12:50:00', '6', '1');
INSERT INTO `tb_blog` VALUES ('54', 'thymeleaf局部刷新表格后，js绑定事件失效', '#### 1、传统绑定事件方式\r\n```jquery\r\n$(\'.m-comment-pass\').on(\'click\', function() {\r\n\r\n})\r\n```\r\n>局部刷新后，会发现单击事件失效。\r\n因为局部刷新table表格后JQ识别不到元素，js事件不会触发，因为它已经替换了，因此，此时需要使用下面方式来进行事件绑定。\r\n\r\n\r\n#### 2、重新绑定事件\r\n```jquery\r\n$(document).on(\'click\', \'.m-comment-pass\', function () {\r\n\r\n})\r\n```', null, '33', '30', '1', '1', null, '1', '2022-03-18 16:59:19', '2022-03-22 16:40:23', '6', '1');
INSERT INTO `tb_blog` VALUES ('57', 'springboot+editormd上传图片功能', '#### **1、引言**\r\n>使用editormd + springboot实现md编辑器的图片上传功能\r\n\r\n#### **2、editormd部分**\r\n2.1通过js开启上传图片功能\r\n>imageFormats : 指定可接收的图片格式(我只列举了我工程所用到的)\r\n>imageUploadURL : 后端处理图片的请求\r\n\r\n```js\r\n imageUpload: true,\r\n imageFormats: [\"jpg\", \"jpeg\", \"png\"],\r\n imageUploadURL: \"http://127.0.0.1:8080/editormd/images\",\r\n onload: function () {\r\n console.log(\"begin upload\");\r\n }\r\n```\r\n#### **3、SpringBoot部分**\r\n3.1 json实体类封装\r\n>因为editormd上传图片后需要服务端返回一个json数据，来判断是否上传成功，并且类型、属性名必须与editormd规定的一致，也就是下述的实体类。\r\n\r\n```java\r\npublic class EditorJson {\r\n    /**\r\n     * editormd上传图片返回的json数据\r\n     */\r\n    private int success;  // 后端是否处理成功 1成功 0失败\r\n    private String message; // 提示信息\r\n    private String url; // 成功后的url地址（图片存储在服务器的地址，相对地址）\r\n}\r\n```\r\n\r\n3.2 请求处理\r\n>注意：请求参数设置为 editormd-image-file\r\n\r\n```java\r\n @PostMapping(\"/editormd/images\")\r\n @ResponseBody\r\n    public EditorJson imagesHandler(@RequestParam(\"editormd-image-file\") MultipartFile file) {\r\n        EditorJson result = new EditorJson();\r\n        try {\r\n            String accessDir = UploadUtils.uploadPictureHandler(file);\r\n            if (accessDir == null || Objects.equals(\"\", accessDir)) {\r\n                result.setSuccess(0);\r\n                result.setMessage(\"错误 : 只支持5MB的jpg,jpeg,png格式的图片\");\r\n            } else {\r\n                result.setSuccess(1);\r\n                result.setMessage(\"upload success\");\r\n                result.setUrl(\"/res/images/\" + accessDir);\r\n            }\r\n        } catch (Exception e) {\r\n            throw new UniversalException(\"图片上传失败,格式或大小错误\");\r\n        }\r\n        return result;\r\n    }\r\n```\r\n\r\n执行流程 ： \r\n\r\n1.后端接收请求后执行 UploadUtils.uploadPictureHandler(file)，【这是我自己封装的处理图片的工具，其实上就是SpringBoot处理文件上传的固定过程，不会的话可以搜索对应的博客】,我这里返回的是文件名在服务器中的名字。\r\n\r\n2.对处理结果进行判断，根据结果设置对应的json数据。\r\n\r\n3.返回json数据。\r\n\r\n\r\n#### **4.效果图**\r\n上传成功效果\r\n![](/res/images/blogPicture/mushroom1647781677830.png)\r\n\r\n显示效果\r\n![](/res/images/blogPicture/mushroom1647781862709.png)\r\n\r\n--------------------------------------------------\r\n\r\n#### **5.过程中出现的错误**\r\n5.1 url不显示\r\n可能是后端返回的json数据格式的问题，**注意：json数据的success属性为数字，不要写成字符串。**\r\n```java\r\nprivate int success; // 正确\r\nprivate String success; //错误\r\n```\r\n\r\n5.2 图片显示不出来\r\n可能是图片在服务器中的相对地址写错了\r\n例如：我的图片存储在 `/static/images`目录下，并且我在`application.yml`中配置了静态资源访问路径为 `/res`，那么我就应该返回此路径：\r\n```java\r\n/res/images/图片名.扩展名\r\n```\r\n\r\n\r\n5.3 editormd的上传文件框错位\r\n打开F12查看元素，是因为那里的css样式被层叠了，只需要将加上 !important 就可以了。不用去修改editormd的css样式，只要在自定义css样式上写上对应的选择器就可以了。\r\n```css\r\n.editormd-form input[type=\"text\"] {\r\n     display: inline-block;\r\n     width: 264px !important;\r\n }\r\n```\r\n\r\n#### **6.结尾**\r\n后端处理请求的代码不能直接完全复制去使用，可根据自己项目的实际情况做修改，但大体处理流程是一样的。\r\n', '/res/images/blogPicture/mushroom1647783203790.png', '32', '64', '1', '1', null, '1', '2022-03-20 21:04:07', '2022-03-23 12:50:00', '6', '1');

-- ----------------------------
-- Table structure for tb_blog_outline
-- ----------------------------
DROP TABLE IF EXISTS `tb_blog_outline`;
CREATE TABLE `tb_blog_outline` (
  `did` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL COMMENT '博客标题',
  `outline` varchar(255) DEFAULT '' COMMENT '博客的概要',
  `views` int NOT NULL COMMENT '博客浏览数',
  PRIMARY KEY (`did`)
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of tb_blog_outline
-- ----------------------------
INSERT INTO `tb_blog_outline` VALUES ('41', 'tomcat修改端口、部署项目', '修改默认端口进入tomcat解压文件夹中依次进入confserver.xml找到里面的connector标签部署工程1.将工程文件夹复制到tomcat安装目录中的webapps文件夹中2.在', '12');
INSERT INTO `tb_blog_outline` VALUES ('44', 'Stream流', 'stream流1.概念及作用将数组或集合的元素全部取出放在类似一条传送带上我们可以在上面操作元素主要作用是为了简化传统数组集合的复杂的操作将操作简单化2.获取stream流', '11');
INSERT INTO `tb_blog_outline` VALUES ('45', 'MyBatis配合PageHepler实现分页', '分页插件使用1.引入pagehelper依赖xmldtd!分页插件dependencygroupdcom.github.pagehelper/group', '6');
INSERT INTO `tb_blog_outline` VALUES ('54', 'thymeleaf局部刷新表格后，js绑定事件失效', '传统绑定事件方式jquery$.mcommentpass.onclick,function局部刷新后会发现单击事件失效因为局部刷新table表格后识别不到元素js事件不会触发', '30');
INSERT INTO `tb_blog_outline` VALUES ('57', 'springboot+editormd上传图片功能', '1引言使用editormd+springboot实现md编辑器的图片上传功能2editormd部分**通过js开启上传图片功能**imageormats指定可接收的图片格式我只列举了我工程所用到的im', '64');

-- ----------------------------
-- Table structure for tb_category
-- ----------------------------
DROP TABLE IF EXISTS `tb_category`;
CREATE TABLE `tb_category` (
  `cid` int NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `uid` int NOT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `description` varchar(200) NOT NULL,
  `picture` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`cid`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of tb_category
-- ----------------------------
INSERT INTO `tb_category` VALUES ('1', 'Java', '6', '2022-03-03 17:16:43', '一门以面向对象思想来编程的语言', '/res/images/category/java.jpg');
INSERT INTO `tb_category` VALUES ('27', 'MyBatis', '6', '2022-02-27 22:22:28', '一个持久层框架', '/res/images/category/1645971748409.jpg');
INSERT INTO `tb_category` VALUES ('32', 'SpringBoot', '6', '2022-03-18 16:49:43', '', '/res/images/category/1647593383975.png');
INSERT INTO `tb_category` VALUES ('33', 'thymeleaf', '6', '2022-03-18 16:51:25', '', '/res/images/category/1647593485615.png');
INSERT INTO `tb_category` VALUES ('34', '测试分类', '6', '2022-03-20 20:32:42', '', '/res/images/category/1647779562038.jpg');

-- ----------------------------
-- Table structure for tb_comment
-- ----------------------------
DROP TABLE IF EXISTS `tb_comment`;
CREATE TABLE `tb_comment` (
  `com_id` int NOT NULL AUTO_INCREMENT,
  `nickname` varchar(20) NOT NULL,
  `mail` varchar(20) NOT NULL,
  `content` varchar(300) NOT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `com_parent_id` int DEFAULT '-1',
  `bid` int NOT NULL,
  `reply_time` timestamp NULL DEFAULT NULL,
  `is_pass` tinyint NOT NULL COMMENT '留言审核是否通过 1通过 0未通过',
  `pass_content` varchar(100) DEFAULT NULL COMMENT '留言审核未通过时的显示文本',
  PRIMARY KEY (`com_id`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of tb_comment
-- ----------------------------
INSERT INTO `tb_comment` VALUES ('31', '小白', 'bai@qq.com', '不错，谢谢分享', '/res/images/commentAvatar/avatar14.jpg', '-1', '45', '2022-03-12 16:01:27', '1', '');
INSERT INTO `tb_comment` VALUES ('32', 'admi', 'admin@qq.com', '谢谢支持', '/res/images/commentAvatar/avatar18.jpg', '31', '45', '2022-03-12 16:01:59', '1', '');
INSERT INTO `tb_comment` VALUES ('33', '小红', 'hong@qq.com', '楼主说的对', '/res/images/commentAvatar/avatar23.jpg', '31', '45', '2022-03-12 16:02:21', '1', '');
INSERT INTO `tb_comment` VALUES ('34', 'Marry', 'Marryaaaaaaaa@qq.com', '确实，有了这个pageInfo对象后，前端分页操作就更简单了', '/res/images/commentAvatar/avatar22.jpg', '-1', '45', '2022-03-12 16:03:50', '1', '');
INSERT INTO `tb_comment` VALUES ('35', 'Jack-a', 'jack@qq.com', '测试留言1', '/res/images/commentAvatar/avatar20.jpg', '-1', '41', '2022-03-12 16:40:45', '1', null);
INSERT INTO `tb_comment` VALUES ('36', 'Marry', 'marry@qq.com', '测试回复1', '/res/images/commentAvatar/avatar4.jpg', '35', '41', '2022-03-12 16:41:14', '1', null);
INSERT INTO `tb_comment` VALUES ('37', '测试', 'ces@qq.com', '测试', '/res/images/commentAvatar/avatar23.jpg', '-1', '46', '2022-03-12 16:49:57', '1', null);
INSERT INTO `tb_comment` VALUES ('38', '测试回复', 'reply@qq.com', '测试', '/res/images/commentAvatar/avatar14.jpg', '37', '46', '2022-03-12 16:50:22', '1', null);
INSERT INTO `tb_comment` VALUES ('39', '小红同学', 'hong@qq.com', '不错', '/res/images/commentAvatar/avatar2.jpg', '-1', '44', '2022-03-12 22:27:07', '1', null);
INSERT INTO `tb_comment` VALUES ('44', '小绿同学', 'lv@qq.com', '可以', '/res/images/commentAvatar/avatar13.jpg', '-1', '44', '2022-03-13 12:37:33', '1', null);
INSERT INTO `tb_comment` VALUES ('48', '1', '1@qq.com', '此留言已被删除', '/res/images/commentAvatar/avatar19.jpg', '-1', '45', '2022-03-13 21:32:01', '1', '');
INSERT INTO `tb_comment` VALUES ('51', '1', '1@qq.com', '测试', '/res/images/commentAvatar/avatar19.jpg', '48', '45', '2022-03-13 21:32:01', '1', '');
INSERT INTO `tb_comment` VALUES ('52', '1', '1@qq.com', '测试', '/res/images/commentAvatar/avatar19.jpg', '48', '45', '2022-03-13 21:32:01', '1', '');
INSERT INTO `tb_comment` VALUES ('54', 'tom', 'tom@qq.com', '可以的', '/res/images/commentAvatar/avatar17.jpg', '-1', '45', '2022-03-17 18:26:03', '1', '');
INSERT INTO `tb_comment` VALUES ('55', 'cherry', 'cherry@163.com', '谢谢博主，解决了', '/res/images/commentAvatar/avatar20.jpg', '-1', '54', '2022-03-18 17:01:14', '1', null);
INSERT INTO `tb_comment` VALUES ('56', 'tom', 'tom@qq.com', '测试', '/res/images/commentAvatar/avatar14.jpg', '-1', '41', '2022-03-19 15:43:12', '1', null);
INSERT INTO `tb_comment` VALUES ('57', 'bu', '23@qq.com', 'hh', '/res/images/commentAvatar/avatar4.jpg', '56', '41', '2022-03-19 15:44:24', '0', '此留言违反了社区规范，已被屏蔽');
INSERT INTO `tb_comment` VALUES ('58', 'marry', 'marry@163.com', '不错', '/res/images/commentAvatar/avatar15.jpg', '-1', '57', '2022-03-20 21:46:12', '1', null);
INSERT INTO `tb_comment` VALUES ('59', '作者', 'mushroom@qqcom', '谢谢支持', '/res/images/commentAvatar/avatar9.jpg', '58', '57', '2022-03-20 21:46:44', '1', null);

-- ----------------------------
-- Table structure for tb_copyright
-- ----------------------------
DROP TABLE IF EXISTS `tb_copyright`;
CREATE TABLE `tb_copyright` (
  `cr_id` int NOT NULL AUTO_INCREMENT,
  `cr_tip` varchar(20) NOT NULL,
  `cr_tip_id` int NOT NULL COMMENT '版权标识',
  PRIMARY KEY (`cr_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of tb_copyright
-- ----------------------------
INSERT INTO `tb_copyright` VALUES ('1', '原创', '1');
INSERT INTO `tb_copyright` VALUES ('2', '转载', '2');
INSERT INTO `tb_copyright` VALUES ('3', '翻译', '3');
INSERT INTO `tb_copyright` VALUES ('4', '原+转', '4');

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `uid` int NOT NULL AUTO_INCREMENT,
  `nickname` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `pwd` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `mail` varchar(20) NOT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `type` int NOT NULL,
  `register_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `mail` (`mail`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES ('1', 'jackZ', '9b04d152845ec0a378394003c96da594', 'jack@qq.com', null, '0', '2022-02-22 16:18:14', '2022-02-28 12:38:45');
INSERT INTO `tb_user` VALUES ('6', 'mushroom', '698d51a19d8a121ce581499d7b701668', '2899268091@qq.com', '/res/images/del_avatar3.jpeg', '0', '2022-02-23 23:15:57', '2022-03-28 12:15:45');
