package com.zs.handler;

import com.zs.pojo.BlogOutline;
import sun.security.x509.AVA;

import java.util.List;
import java.util.Random;

/**
 * @Created by zs on 2022/2/23.
 */
public class RandomUtils {

    private static final String BASE_NUM = "1234567890";
    private static Random random = new Random();

    /**
     * 根据指定边界，随机生成一个数
     * @param base
     * @return
     */
    public static Integer generateNum(Integer base) {
        if (base == 0) {
            return 0;
        }
        return random.nextInt(base);
    }

    /**
     * 随机生成数字验证码
     * @return
     */
    public static String generateRandomNum() {
        StringBuilder sb = new StringBuilder();
        for (int v = 1; v < 5;  v++) {
            int index = random.nextInt(BASE_NUM.length());
            sb.append(BASE_NUM.charAt(index));
        }
        return sb.toString();
    }

    /**
     * 随机选取一个博客概要对象
     * @param list
     * @return
     */
    public static BlogOutline generateBlogOutline(List<BlogOutline> list) {
        if (list.size() <= 0) {
            throw new UniversalException("未查询到推荐文章");
        }
        return list.get(random.nextInt(list.size()));
    }

    /**
     * 随机返回一个评论头像地址 (基数为23个头像 static/images/commentAvatar/)
     * @return
     */
    public static String generateAvatar() {
        String avatarDir = "/res/images/commentAvatar/avatar"+random.nextInt(23+1)+".jpg";
        return avatarDir;
    }

}
