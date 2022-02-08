package com.ll.youth_learn.controller;

import com.ll.youth_learn.entity.TopOrg;
import com.ll.youth_learn.service.IOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
public class OrgController {

    @Autowired
    private IOrgService orgService;

    @RequestMapping("/getTopOrgList")
    @ResponseBody
    public List<TopOrg> getTopOrgList() throws Exception {
        return orgService.selectTopOrgList();
//        return JSON.toJSONString(orgService.selectTopOrgList()); /*使用该语句时要将返回值类型改为String!*/
    }
}
