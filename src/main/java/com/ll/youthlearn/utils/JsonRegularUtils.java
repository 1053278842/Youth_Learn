package com.ll.youthlearn.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/3/9 14:23
 * @Description :
 */
public class JsonRegularUtils {

    /**
     * 获取memberList用的,仅仅满足获取{code:"",list:list:[{...},{...}]}该格式。
     * @param jsonData
     * @return 返回JsonArray
     */
    public static JSONArray getMemberListByJsonData(String jsonData){
        return JSON.parseObject(jsonData).getJSONObject("list").getJSONArray("list");
    }
}
