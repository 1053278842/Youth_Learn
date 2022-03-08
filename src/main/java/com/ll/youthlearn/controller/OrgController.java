package com.ll.youthlearn.controller;

import com.ll.youthlearn.entity.Org;
import com.ll.youthlearn.entity.TopOrg;
import com.ll.youthlearn.factory.IPythonSpider;
import com.ll.youthlearn.service.IOrgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
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

    @Resource(name = "pythonSpider")
    private IPythonSpider pythonSpider;

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

        //获取父组织的id主键,作为子组织的parent_id
        String path=orgs[orgs.length-1].getPath();
        String parentStr = orgs[orgs.length-1].getName();
        Integer parentId = orgService.selectOneByName(path).getId();

        List<Org> results_orgs = pythonSpider.getOrgJsonByName(orgs);

        for (Org o:results_orgs) {
            o.setParentId(parentId);
            o.setPath(path+"|"+o.getName());
        }

        try {
            orgService.insertManyOrg(results_orgs);
        } catch (Exception e) {

        }

        return results_orgs;
    }

}
