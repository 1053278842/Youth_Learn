package com.ll.youthlearn.controller;

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
        Integer userId = ((User)session.getAttribute("USER_INFO")).getId();
        ModelAndView mv=new ModelAndView();

        List<Stage> stages=stageService.findStagesByUserId(userId);
        mv.addObject("STAGE_LIST",stages);

        mv.setViewName("member-last");
        return mv;
    }
}
