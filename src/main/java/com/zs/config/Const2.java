package com.zs.config;

/**
 * 状态码宏
 *      单数：失败
 *      复数：成功
 * @Created by zs on 2022/4/20.
 */
public class Const2 {

    public static final String DEFAULT_WRITER_AVATAR = "common_avatar.png"; // 注册时默认用户头像
//    public static final String ARTICLE_PICTURE_SAVE_DIR_WINDOWS = "C:/Users/Air/Desktop/blog/blog_static/images/fp/";
//    public static final String USER_AVATAR_SAVE_DIR_WINDOWS = "C:/Users/Air/Desktop/blog/blog_static/images/avatar/";
    public static final String ARTICLE_PICTURE_SAVE_DIR_WINDOWS = "C:/Users/Air/Desktop/blog/blog_v2.0-frontEnd/blog_static/images/fp/";
    public static final String USER_AVATAR_SAVE_DIR_WINDOWS = "C:/Users/Air/Desktop/blog/blog_v2.0-frontEnd/blog_static/images/avatar/";
    public static final String ARTICLE_PICTURE_SAVE_DIR_LINUX = "/usr/local/server/nginx/blog_static/images/fp/";
    public static final String USER_AVATAR_SAVE_DIR_LINUX = "/usr/local/server/nginx/blog_static/images/avatar/";


    public static final Integer GLOBAL_EXCEPTION = -1; // 全局异常
    public static final Integer PARAMETER_FAIL = 301; // 请求参数不符合规则

    public static final Integer NOT_FOUND = 404; // 访问资源不存在
    public static final Integer NO_LOGIN = 500; // 未登录
    public static final Integer TOKEN_EXPIRE = 501; // token过期、伪造
    public static final String TOKEN_PWD = "JHSEFSCB"; // token生成密码
    public static final Integer SERVICE_SUCCESS = 200; // 业务执行成功
    public static final Integer SERVICE_FAIL = 525; // 业务执行失败
    public static final Integer PARAMETERS_IS_NULL = 505; // 请求参数为空
    public static final Integer REGISTER_FAIL = 511; // 用户注册失败
    public static final Integer REGISTER_SUCCESS = 512; // 用户注册成功
    public static final Integer REGISTER_FAIL_VALIDATE_CODE = 513; // 用户注册失败，验证码错误
    public static final Integer LOGIN_SUCCESS = 522; // 登录成功
    public static final Integer LOGIN_FAIL = 523; // 登录失败
    public static final Integer LOGIN_FAIL_TOKEN = 525; // 登录失败,token生成失败
    public static final Integer UPLOAD_FILE_FAIL_EXT = 531; // 上传文件失败，文件类型不支持
    public static final Integer UPLOAD_FILE_FAIL_NULL = 533; // 上传文件失败，文件为空
    public static final Integer UPLOAD_FILE_FAIL_SIZE = 535; // 上传文件失败，超过规定文件大小
    public static final Integer UPLOAD_FILE_FAIL_EXCEPTION = 537; // 上传文件失败，处理异常
    public static final Integer UPLOAD_FILE_SUCCESS = 538; // 上传文件成功

    public static final Integer WRITER_NAME_REPEAT = 601; // 用户名重复
    public static final Integer WRITER_NAME_AVAILABLE = 602; // 用户名可用
    public static final Integer MAIL_REPEAT = 603; // 邮箱已被注册
    public static final Integer MAIL_AVAILABLE = 604; // 邮箱可用
    public static final Integer SEND_MAIL_FAIL = 611; // 验证码邮箱发送失败
    public static final Integer SEND_MAIL_SUCCESS = 612; // 验证码邮箱发送成功

    public static final Integer ARTICLE_COMMENT_PAGE_SIZE = 2; // 文章评论每页显示条数

    /**
     * 用户已经关注(用户)
     */
    public static final Integer ALREADY_FOLLOW = 701;

    /**
     * 用户未关注(用户)
     */
    public static final Integer NO_FOLLOW = 702;

    /**
     * 用户还未给文章点赞
     */
    public static final Integer NO_LIKE = 753;

    /**
     * 用户已经为文章点赞
     */
    public static final Integer ALREADY_LIKE = 754;

    /**
     * 取消点赞成功(文章)
     */
    public static final Integer CANCEL_LIKE_SUCCESS = 756;

    /**
     * 取消点赞失败(文章)
     */
    public static final Integer CANCEL_LIKE_FAIL = 755;


    /**
     * 同步任务执行成功
     */
    public static final Integer SYNC_SUCCESS = 1000;

    /**
     * 同步任务执行成功,但无影响记录
     */
    public static final Integer SYNC_SUCCESS_NO_OPERATION = 1001;

    /**
     * elasticSearch文章信息索引名
     */
    public static final String ES_ARTICLE_INDEX = "zblogarticlesindex";

    /**
     * elasticSearch用户信息索引名
     */
    public static final String ES_USERINFO_INDEX = "zbloguserinfoindex";

    /**
     * 队列名称1
     */
    public static final String MSG_QUEUE_1 = "MsgQueue1";

}
