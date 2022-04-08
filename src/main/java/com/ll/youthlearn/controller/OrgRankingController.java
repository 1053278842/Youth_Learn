package com.ll.youthlearn.controller;

import com.ll.youthlearn.entity.OrgRanking;
import com.ll.youthlearn.entity.User;
import com.ll.youthlearn.service.IOrgRankingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author ll
 */
@Slf4j
@Controller
@RequestMapping("/OrgRanking")
public class OrgRankingController {

    private final IOrgRankingService orgRankingService;

    public OrgRankingController(IOrgRankingService orgRankingService) {
        this.orgRankingService = orgRankingService;
    }

    @RequestMapping("/OneStage")
    public ModelAndView OneStage(HttpSession session){
        User current_user=(User)session.getAttribute("USER_INFO");
        Integer uid= current_user.getId();

        List<OrgRanking> orgRankingList = orgRankingService.findCurrentStageRanking(uid);

        ModelAndView mv=new ModelAndView();
        mv.setViewName("org-ranking-list");
        mv.addObject("ONE_STAGE_LIST",orgRankingList);

        return mv;
    }

    @RequestMapping("/RangeRanking")
    public ModelAndView RangeRanking(HttpSession session,Integer startIndex,Integer stageCountNums){
        User current_user=(User)session.getAttribute("USER_INFO");
        Integer uid= current_user.getId();

        if(startIndex<0){startIndex=0;}
        if(stageCountNums<0){stageCountNums=0;}

        List<OrgRanking> orgRankingList = orgRankingService.findRangeStageRanking(uid,startIndex,stageCountNums);

        ModelAndView mv=new ModelAndView();
        mv.setViewName("org-ranking-list");
        mv.addObject("ONE_STAGE_LIST",orgRankingList);

        return mv;
    }


}
