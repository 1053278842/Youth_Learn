package com.ll.youthlearn.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/2/24 23:50
 * @Description :
 */
@Slf4j
public class PythonUtils {
    public static void RunPythonFile(String[] args){
        try {
            Process proc = Runtime.getRuntime().exec(args);
            //用输入输出流来截取结果
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(),"utf-8"));
            String line = null;
            while ((line = in.readLine()) != null) {
                log.info(line);
            }
            in.close();
            proc.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将形如”xxx|xx|xx“格式的路径规划为形如”['xx','xx,'xx']“的python用路径格式
     * @param org
     * @return
     */
    public static String RegulatePath(String org){
        //拼接param:xxx|xx->["name1","level2Name"]
        String orgPathStrParam="";
        String[] orgPath_TempSplitArray=org.split("\\|");

        orgPathStrParam+="[";
        for (String s:orgPath_TempSplitArray) {
            orgPathStrParam+="'"+s+"',";
        }
        //去掉最后一个‘,'
        orgPathStrParam=orgPathStrParam.substring(0,orgPathStrParam.length()-1);
        orgPathStrParam+="]";
        return orgPathStrParam;
    }

    /**
     * ['直属高校', '合肥学院', '外国语学院', '2018级朝鲜语2+2（2）班'] To 2018级朝鲜语2+2（2）班
     * @param org
     * @return
     */
    public static String ExtractLastOrgNameFromArrayStr(String org){

        String result="";

        String[] strings=org.split(",");
        String str=strings[strings.length-1];
        str=str.trim();
        str=str.substring(1,str.length()-2);

        result=str;
        return result;
    }

    public static int RegulateNumberRange(Integer value,Integer minValue,Integer maxValue){
        //maxStage的范围
        if(value<minValue){
            value=minValue;
        }
        if(value>maxValue){
            value=maxValue;
        }
        return value;
    }
}
