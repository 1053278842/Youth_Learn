package com.ll.youthlearn.controller;

import com.ll.youthlearn.entity.User;
import com.ll.youthlearn.factory.IPythonSpider;
import com.ll.youthlearn.service.IOrgPathService;
import com.ll.youthlearn.service.impl.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/2/16 22:11
 * @Description :
 */
@Controller
@Slf4j
@RequestMapping("/MemberEachStage")
public class MemberEachStageController {

    private final IOrgPathService orgPathService;

    @Resource(name = "pythonSpider")
    private IPythonSpider pythonSpider;

    private final MemberServiceImpl memberService;

    public MemberEachStageController(MemberServiceImpl memberService, IOrgPathService orgPathService) {
        this.memberService = memberService;
        this.orgPathService = orgPathService;
    }


    @ResponseBody
    @RequestMapping("/addMemberEachStage")
    public String addMemberEachStage(HttpSession session,Integer maxStage){

        Integer id = ((User)session.getAttribute("USER_INFO")).getId();
        String orgPath=((User)session.getAttribute("USER_INFO")).getOrgPath();

        pythonSpider.saveMemberEachStage(id,orgPath,maxStage);

        return "";
    }
}
