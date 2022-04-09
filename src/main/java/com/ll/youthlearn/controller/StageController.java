package com.ll.youthlearn.controller;

import com.ll.youthlearn.entity.OrgPath;
import com.ll.youthlearn.entity.Stage;
import com.ll.youthlearn.entity.User;
import com.ll.youthlearn.factory.IPythonSpider;
import com.ll.youthlearn.service.IMemberService;
import com.ll.youthlearn.service.IOrgPathService;
import com.ll.youthlearn.service.IStageService;
import com.ll.youthlearn.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.Date;
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

    @Resource(name = "pythonSpider")
    private IPythonSpider pythonSpider;

    private final IMemberService memberService;
    private final IOrgPathService orgPathService;
    private final IStageService stageService;

    public StageController(IStageService stageService, IMemberService memberService, IOrgPathService orgPathService) {
        this.stageService = stageService;
        this.memberService = memberService;
        this.orgPathService = orgPathService;
    }

    /**
     * 跟新所有stage的接口
     * @return 是否更新成功
     */
    @ResponseBody
    @RequestMapping("/updateStage")
    public String updateStage(){
        pythonSpider.saveAllStage();
        return "success";
    }

    @ResponseBody
    @RequestMapping("/getStageByLimitNum")
    public List<Stage> getStageByLimitNum(@RequestParam(defaultValue = "1") Integer num){

        if(num<=0){num=1;}

        List<Stage> stages = stageService.findNewStageByNum(num);
        return stages;
    }

    @RequestMapping("/getAllStageByUid")
    public ModelAndView getAllStageByUid(HttpSession session) throws Exception {

        //获取当前周的期次
        Stage currentStage=stageService.findNewestStage();

        User current_user=(User)session.getAttribute("USER_INFO");
        Integer userId = current_user.getId();
        OrgPath current_OrgPath= current_user.getCurrent_path();
        Integer pathId=current_OrgPath.getId();

        Integer maxMemberNumber=0;
        if(current_OrgPath!=null){
            //当前组织路径下的最大成员数
            maxMemberNumber=current_OrgPath.getMaxMemberNumber();
            if(maxMemberNumber==null){
                maxMemberNumber=0;
            }
        }

        ModelAndView mv=new ModelAndView();

        List<Stage> stages=stageService.findStagesByUserId(userId,pathId);
        if(stages==null||stages.size()==0){
            mv.addObject("STAGE_LIST",stages);
            mv.addObject("ORG_PATH_MAX_MEMBER_NUMBER",maxMemberNumber);
            mv.setViewName("member-last");
            return mv;
        }
        //去除lastStageList中包含currentStage的情况,因为列表按照时间排序,故只判断第一个元素
        if(currentStage!=null&&(stages.get(0).getId()==currentStage.getId())){
            stages.remove(0);
        }

        //过滤该组织从未参加过的以前的期次;规则：从第一期开始删，直到遇到存在member的stage为止
        int boundIndex= stages.size()-1;
        for (int i = stages.size()-1; i >= 0; i--) {
            if (stages.get(i).getMembers()!=null&&stages.get(i).getMembers().size()>0){
                boundIndex=i;
                break;
            }
        }
        stages=stages.subList(0,boundIndex+1);

        //设置stage对象的stageDate
        //规则：存在member的情况下,stageDate等于member所在日期内的周一的日期
        for (Stage s:stages) {
            if(s.getMembers()!=null&&s.getMembers().size()!=0){
                Timestamp memberDate=s.getMembers().get(0).getTimestamp();

                String first_day_week="";
                if(memberDate!=null){
                    Date date = new Date(memberDate.getTime());
                    first_day_week = DateUtils.getWeekMondayDate("yyyy-MM-dd",date);
                }

                s.setStageDate(first_day_week);
            }else{
                s.setStageDate("1893-12-26");
            }

        }
        mv.addObject("STAGE_LIST",stages);
        mv.addObject("ORG_PATH_MAX_MEMBER_NUMBER",maxMemberNumber);

        mv.setViewName("member-last");
        return mv;
    }
}
