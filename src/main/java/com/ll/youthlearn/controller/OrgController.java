package com.ll.youthlearn.controller;

import com.ll.youthlearn.entity.Org;
import com.ll.youthlearn.entity.TopOrg;
import com.ll.youthlearn.service.IOrgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
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
 * @CreateTime :  2022/2/9 1:34
 * @Description :
 */
@RequestMapping("/Org")
@Controller
@Slf4j
public class OrgController {

    @Resource(name = "orgService")
    private IOrgService orgService;


    @RequestMapping("/getTopOrgList")
    @ResponseBody
    public List<TopOrg> getTopOrgList() throws Exception {
        return orgService.selectTopOrgList();
        //return JSON.toJSONString(orgService.selectTopOrgList()); /*使用该语句时要将返回值类型改为String!*/
    }

    @ResponseBody
    @RequestMapping("/getOrgsAllStage")
    public List<Org> getOrgsAllStageByNames(@RequestBody Org[] orgs ){
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
        String location = System.getProperty("user.dir") + "\\src\\main\\java\\com\\ll\\youthlearn\\python\\GetOrgJsonByName_Spider.py";
        String param=paramStr;
        try {
            String[] args=new String[]{"python", location,param};
            proc = Runtime.getRuntime().exec(args);
            //用输入输出流来截取结果
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(),"gbk"));
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
}
