package com.ll.youthlearn.controller;

import com.ll.youthlearn.entity.OrgPath;
import com.ll.youthlearn.entity.Stage;
import com.ll.youthlearn.entity.User;
import com.ll.youthlearn.service.IStageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/2/25 16:24
 * @Description :
 */
@RequestMapping("/Stage")
@Controller
@Slf4j
public class StageController {

    private final IStageService stageService;

    public StageController(IStageService stageService) {
        this.stageService = stageService;
    }

    @RequestMapping("/getAllStageByUid")
    public ModelAndView getAllStageByUid(HttpSession session){
        User current_user=(User)session.getAttribute("USER_INFO");
        Integer userId = current_user.getId();
        OrgPath current_OrgPath= current_user.getCurrent_path();

        //当前组织路径下的最大成员数
        Integer maxMemberNumber=current_OrgPath.getMaxMemberNumber();
        if(maxMemberNumber==null){
            maxMemberNumber=0;
        }

        //TODO 更新User的Current_Path,应为只有第一次登录时才会更新！

        ModelAndView mv=new ModelAndView();

        List<Stage> stages=stageService.findStagesByUserId(userId);
        mv.addObject("STAGE_LIST",stages);
        mv.addObject("ORG_PATH_MAX_MEMBER_NUMBER",maxMemberNumber);

        mv.setViewName("member-last");
        return mv;
    }
}
