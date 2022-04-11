package com.ll.youthlearn.controller;

import com.ll.youthlearn.entity.OrgRanking;
import com.ll.youthlearn.entity.User;
import com.ll.youthlearn.service.IOrgRankingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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

    @ResponseBody
    @RequestMapping("/RangeRanking")
    public List<OrgRanking> RangeRanking(HttpSession session,Integer startIndex,Integer stageCountNums){
        User current_user=(User)session.getAttribute("USER_INFO");
        Integer uid= current_user.getId();

        if(startIndex<0){startIndex=0;}
        if(stageCountNums<0){stageCountNums=0;}

        List<OrgRanking> orgRankingList = orgRankingService.findRangeStageRanking(uid,startIndex,stageCountNums);

        return orgRankingList;
    }

    /**
     * 后端返回的数据距离能用还多了一层[],前端注意要eval()一下
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping("/getEchartsLineData")
    public List<Object[]> getEchartsLineData(HttpSession session){
        User current_user=(User)session.getAttribute("USER_INFO");
        Integer uid= current_user.getId();

        //该图标一次统计近X期的数据
        final Integer maxStageNums=10;

        return orgRankingService.getEchartsLineRankingDate(uid,maxStageNums);
    }

}
