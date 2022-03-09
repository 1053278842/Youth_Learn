package com.ll.youthlearn.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/3/9 14:27
 * @Description :
 */
public class UrlRegularUtils {
    /**
     * 用户传入接口url
     * @param url 例如https://www.baidu.com/user/ll
     * @param stageName 指定stage的name值
     * @return 拼接第一参数，如https://www.baidu.com/user/ll?money=999
     */
    public static String setStage(String url,String stageName){
        return url+"?table_name="+stageName;
    }

    /**
     * 将固定格式的Org Path根据一定格式添加到url中并返回
     * @param url 使用了?指定过一次携带参数的url，如：http://ll.com/user?money=999
     * @param orgPath 字符串，形如“|直属高校|东京大学|美术系|xxx”;
     * @return 返回拼接好的url
     * @throws UnsupportedEncodingException
     */
    public static String setLevelParams(String url,String orgPath) throws UnsupportedEncodingException {
        String[] pathArray=orgPath.split("\\|");
        for (int i = 0; i < pathArray.length; i++) {
            String opthStr= URLEncoder.encode(pathArray[i], "utf-8");
            url+= MessageFormat.format("&level{0}={1}",i+1, opthStr);
        }
        return url;
    }

    /**
     * 根据stat的状态值0/1，为URL头部添加协议
     * @param url 形如www.abc.com/xx/xx/.. 的不包含协议头的url
     * @param isHTTPS 0/1，0：https安全协议；1：http协议
     * @return 在url添加http://并返回
     */
    public static String setRequestProtocolHeader(String url, boolean isHTTPS) {
        String results=url;
        if (isHTTPS){
            results="https://"+results;
        }else{
            results="http://"+results;
        }
        return  results;
    }
}
