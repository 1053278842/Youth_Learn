package com.ll.youthlearn.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/3/1 16:20
 * @Description :
 */
public class DateUtils {
    /**
     * 传入日期即可获得该日期所在周的周一日期
     * @param format 日期格式形如: yyyy-MM-dd HH:mm:ss / yyyy-MM-dd
     * @param date   所要提取的日期，Date类型
     * @return  format指定的字符串格式化日期
     */
    public static String getWeekMondayDate(String format,Date date){
        DateFormat sdf = new SimpleDateFormat(format);

        Calendar cal = Calendar.getInstance();

        cal.setTime(date);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String first_day_week = sdf.format(cal.getTime());

        return first_day_week;
    }
}
