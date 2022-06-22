package com.zs.handler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * @author Air
 * @create 2022-06-05 10:40
 */
public class DateUtils {

    /**
     * 日期转字符串
     * @param date
     * @param pattern 日期格式
     * @return
     */
    public static String dateToString(Date date, String pattern) {
        if (Objects.isNull(date) || Objects.isNull(pattern) || Objects.equals("", pattern)) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 字符串转日期
     * @param str
     * @param pattern 日期格式
     * @return
     */
    public static Date stringToDate(String str, String pattern) throws ParseException {
        if (Objects.isNull(str) || Objects.isNull(pattern) || Objects.equals("", pattern)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.parse(str);
    }
}
