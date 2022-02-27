package com.ll.youthlearn.controller;

import com.ll.youthlearn.entity.Member;
import com.ll.youthlearn.entity.User;
import com.ll.youthlearn.factory.IPythonSpider;
import com.ll.youthlearn.service.impl.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

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
@RequestMapping("/Member")
public class MemberController {

    @Resource(name = "pythonSpider")
    private IPythonSpider pythonSpider;

    private final MemberServiceImpl memberService;

    public MemberController(MemberServiceImpl memberService) {
        this.memberService = memberService;
    }

    /**
     * 根据传入的用户主键id，返回联动相关的member列表
     * @param userId 用户主键，session.USER_INFO.id提供
     * @return 联动相关的member列表
     * @throws Exception n
     * @ps ajax调用时新生页面会调用不到model值，推测是因为model内的值都给fragment中的片段填充了。解决办法是多设置几个model变量
     */
    @RequestMapping("/getMembersJson")
    public ModelAndView getMemberJson(int userId, boolean isAsc) throws Exception {

        ModelAndView mv=new ModelAndView();

        //获取member列表，同时作为信息源
        List<Member> memberList=memberService.selectMemberByUserIdAndOrder(userId,isAsc);

        //获取组织人数
        Integer orgNums=memberList.size();

        //获取平均times
        float timesAver=0;
        float totalTimes = 0;
        for (Member m:memberList) {
            totalTimes+=m.getTimes();
        }
        timesAver=totalTimes/orgNums;

        //统计的有效期次
        Integer effectiveStatisticsStage=0;
        if(!memberList.isEmpty()){
            effectiveStatisticsStage=memberList.get(0).getMaxTimes();
        }

        mv.addObject("MEMBER_LIST",memberList);
        mv.addObject("MEMBER_LIST_NUMBER",orgNums);
        mv.addObject("MEMBER_LIST_AverTimes",timesAver);
        mv.addObject("MEMBER_LIST_STATUS",isAsc);
        mv.addObject("MEMBER_LIST_MAXIMS",effectiveStatisticsStage);
        mv.setViewName("member-table::page_member_table");
        return mv;
    }

    /**
     * 根据传入的Id和email对member重新设置email.
     * @param memberId
     * @param email
     * @return  返回更新结果，0=false;1=true
     * @throws Exception n
     */
    @ResponseBody
    @RequestMapping("/updateMemberById")
    public int updateMemberById(Integer memberId, String email) throws Exception {
        int resultInt=memberService.updateOneWithId(memberId,email);
        return resultInt;
    }


    /**
     * 根据指定的id删除member
     * @param memberId
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/delMemberById")
    public int delMemberById(Integer memberId) throws Exception {
        int resultInt=memberService.deleteOneWithId(memberId);
        return resultInt;
    }

    @RequestMapping("/addMemberManual")
    public String addMemberManual(@RequestParam(value = "name-input") String memberName,@RequestParam(value = "email-input") String memberEmail,
                               @RequestParam(value = "maxTimes-input")Integer maxTimes,@RequestParam(value = "parentId-input")Integer parentId,
                               @RequestParam(value = "path-input") String path) throws Exception {
        int resultInt=memberService.addMemberByNameAndEmail(memberName,memberEmail,maxTimes,parentId,path);
        return "redirect:/memberTable";
    }

    @RequestMapping("/addMemberByPath")
    public String addMemberByPath(@RequestParam(value = "id") int id,@RequestParam(value = "orgPath") String orgPath,int maxStage){

        pythonSpider.saveMemberOfOnePath(id,orgPath,maxStage);
        return "member-table";
    }

    @RequestMapping("/getLastMemberData")
    public ModelAndView getLastMemberData(HttpSession session){
        Integer id = ((User)session.getAttribute("USER_INFO")).getId();
        String orgPath=((User)session.getAttribute("USER_INFO")).getOrgPath();

        //获取stage页data


        ModelAndView mv=new ModelAndView();

        mv.setViewName("member-last");
        return mv;
    }

    @RequestMapping("/addMemberEachStage")
    public ModelAndView addMemberEachStage(HttpSession session,Integer maxStage){
        Integer id = ((User)session.getAttribute("USER_INFO")).getId();
        String orgPath=((User)session.getAttribute("USER_INFO")).getOrgPath();

        //TODO 统计一下增加的条数！
        pythonSpider.saveMemberEachStage(id,orgPath,maxStage);

        ModelAndView mv=new ModelAndView();
        mv.setViewName("member-last");
        return mv;
    }
}
