package com.ll.youthlearn.factory;

import com.ll.youthlearn.entity.Org;
import com.ll.youthlearn.utils.PythonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/2/24 23:21
 * @Description :
 */
@Slf4j
@Component(value = "pythonSpider")
public class PythonSpiderImpl implements IPythonSpider {


    @Override
    public void saveAllStage() {

        String pyLocation=location+"GetAllStageToMySQL_Spider.py";
        String[] args=new String[]{"python", pyLocation};
        PythonUtils.RunPythonFile(args);

    }

    @Override
    public void saveMemberOfOnePath(int id,String orgPath,int maxStage,int pathId) {
        //maxStage的范围
        maxStage=PythonUtils.RegulateNumberRange(maxStage,0,80);

        //拼接param:xxx|xx->["name1","level2Name"]
        String orgPathStrParam=PythonUtils.RegulatePath(orgPath);

        //转换id参数
        String userStrId=String.valueOf(id);
        String maxStageStr=String.valueOf(maxStage);
        String pathIdStr=String.valueOf(pathId);

        String pyLocation=location+"GetMemberByPathToMySQL.py";
        String[] args=new String[]{"python", pyLocation,userStrId,orgPathStrParam,maxStageStr,pathIdStr};

        PythonUtils.RunPythonFile(args);
    }

    @Override
    public void saveMemberEachStage(int id, String orgPath, int maxStage, Integer pathId, Integer inNewStage) {
        maxStage=PythonUtils.RegulateNumberRange(maxStage,0,80);

        //拼接param:xxx|xx->["name1","level2Name"]
        String orgPathStrParam=PythonUtils.RegulatePath(orgPath);

        //转换id参数
        String userStrId=String.valueOf(id);
        String maxStageStr=String.valueOf(maxStage);
        String pathStr=String.valueOf(pathId);
        String inNewStageStr=String.valueOf(inNewStage);

        String pyLocation=location+"GetMemberEachStageToMySQL.py";
        String[] args=new String[]{"python", pyLocation,userStrId,orgPathStrParam,maxStageStr,pathStr,inNewStageStr};

        PythonUtils.RunPythonFile(args);
    }

    @Override
    public List<Org> getOrgJsonByName(Org[] orgs) {
        //拼接param:org对象->["name1","level2Name"]
        String paramStr="";
        paramStr+="[";
        for (Org o:orgs) {
            paramStr+="'"+o.getName()+"',";
        }
        //去掉最后一个‘,'
        paramStr=paramStr.substring(0,paramStr.length()-1);
        paramStr+="]";

        //调用python
        //定义回传结果类型
        List<Org> results_orgs=new ArrayList<Org>();

        Process proc;
        String pyLocation=location+"GetOrgJsonByName_Spider.py";

        String param=paramStr;
        try {
            String[] args=new String[]{"python", pyLocation,param};
            proc = Runtime.getRuntime().exec(args);
            //用输入输出流来截取结果
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(),"utf-8"));
            String line = null;
            while ((line = in.readLine()) != null) {
                Org tempOrg=new Org();
                tempOrg.setName(line);
                results_orgs.add(tempOrg);
            }
            in.close();
            proc.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return results_orgs;
    }

    @Override
    public boolean checkOrgPathAvailable(String orgPath) {

        //拼接param:xxx|xx->["name1","level2Name"]
        String orgPathStrParam=PythonUtils.RegulatePath(orgPath);

        String pyLocation=location+"CheckOrgPathAvailable.py";
        String[] args=new String[]{"python", pyLocation,orgPathStrParam};

        final String avail ="True";
        final String notAvail ="False";
        //默认不通过
        String resultStr=notAvail;

        Process proc;
        try {
            proc = Runtime.getRuntime().exec(args);
            //用输入输出流来截取结果
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(),"utf-8"));
            String line = null;
            while ((line = in.readLine()) != null) {
                resultStr=line;
            }
            in.close();
            proc.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(resultStr.equals(avail)){
            return true;
        }else{
            return false;
        }
    }
}
