package com.ll.youthlearn.controller;

import com.ll.youthlearn.entity.User;
import com.ll.youthlearn.factory.IPythonSpider;
import com.ll.youthlearn.service.IMemberEachStageService;
import com.ll.youthlearn.service.IMemberService;
import com.ll.youthlearn.service.IOrgPathService;
import com.ll.youthlearn.service.IStageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;

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

    @Resource(name = "pythonSpider")
    private IPythonSpider pythonSpider;

    private final IOrgPathService orgPathService;
    private final IMemberEachStageService memberEachStageService;
    private final IMemberService memberService;
    private final IStageService stageService;

    public MemberEachStageController(IMemberService memberService, IOrgPathService orgPathService, IMemberEachStageService memberEachStageService, IStageService stageService) {
        this.memberService = memberService;
        this.orgPathService = orgPathService;
        this.memberEachStageService = memberEachStageService;
        this.stageService = stageService;
    }


    @ResponseBody
    @RequestMapping("/addMemberEachStage")
    public String addMemberEachStage(HttpSession session,Integer maxStage){

        Integer id = ((User)session.getAttribute("USER_INFO")).getId();
        String orgPath=((User)session.getAttribute("USER_INFO")).getCurrent_path().getOrgPath();

        //删除多余的memberEachStage数据,只在maxStage>MySQL:maxStage情况下发生
        memberEachStageService.deleteMemberEachStageByUserIdAndMaxStage(id,maxStage);

        log.warn(id+orgPath+maxStage);
        pythonSpider.saveMemberEachStage(id,orgPath,maxStage);

        return "";
    }

    /**
     * 该接口自动执行
     * @return
     */
    @ResponseBody
    @RequestMapping("/getCurrentStageMember")
    public String getCurrentStageMember(HttpSession session,Integer maxStage) throws IOException {

        return "";
    }
}
