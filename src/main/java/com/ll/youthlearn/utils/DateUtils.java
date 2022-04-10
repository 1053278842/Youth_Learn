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

        //-1是为了修正cal类以周日为每星期第一天的设定
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK)-1;
        //day_of_week-1==0的情况对应为当前是周日,周日day_of_week=1,修正为周日=7.
        if(day_of_week==0){
            day_of_week=7;
        }
        //+1为了回到cal周日是一周第一天的设置
        cal.add(Calendar.DATE,-day_of_week+1);

        String first_day_week = sdf.format(cal.getTime());
        if(format.equals("yyyyMMdd")){
            first_day_week = sdf.format(cal.getTime())+"00000000";
        }

        return first_day_week;
    }

    /**
     * 传入日期即可获得该日期所在周的周一日期
     * @param format 日期格式形如: yyyy-MM-dd HH:mm:ss / yyyy-MM-dd
     * @param date   所要提取的日期，Date类型
     * @return  format指定的字符串格式化日期
     */
    public static String getWeekSundayDate(String format,Date date){
        DateFormat sdf = new SimpleDateFormat(format);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        //-1是为了修正cal类以周日为每星期第一天的设定
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK)-1;
        //day_of_week-1==0的情况对应为当前是周日,周日day_of_week=1,修正为周日=7.
        if(day_of_week==0){
            day_of_week=7;
        }
        //+1为了回到cal周日是一周第一天的设置
        cal.add(Calendar.DATE,-day_of_week+7);


        String first_day_week = sdf.format(cal.getTime());
        if(format.equals("yyyyMMdd")){
            first_day_week = sdf.format(cal.getTime())+"235959999";
        }

        return first_day_week;

    }

    /**
     * 计算两日期相差的天(小数),两日期大小不需要比较，位置随意
     * @param smallDate  两变量中较小的一个Date
     * @param bigDate    两变量中较大的一个Date
     * @return  double 天
     */
    public static double getBetweenDate(Date smallDate,Date bigDate){
        Calendar cal = Calendar.getInstance();
        if(smallDate.getTime()<=bigDate.getTime()){
            cal.setTime(smallDate);
            Long time1=cal.getTimeInMillis();
            cal.setTime(bigDate);
            Long time2=cal.getTimeInMillis();
            return (time2-time1)*1.0/(1000*3600*24);
        }else{
            cal.setTime(bigDate);
            Long time1=cal.getTimeInMillis();
            cal.setTime(smallDate);
            Long time2=cal.getTimeInMillis();
            return (time2-time1)*1.0/(1000*3600*24);
        }
    }
}
