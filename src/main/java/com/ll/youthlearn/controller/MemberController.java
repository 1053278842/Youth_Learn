package com.ll.youthlearn.controller;

import com.ll.youthlearn.entity.Member;
import com.ll.youthlearn.entity.OrgPath;
import com.ll.youthlearn.entity.User;
import com.ll.youthlearn.factory.IPythonSpider;
import com.ll.youthlearn.service.IMailService;
import com.ll.youthlearn.service.IOrgPathService;
import com.ll.youthlearn.service.IStageService;
import com.ll.youthlearn.service.impl.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.mail.MessagingException;
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

    private final IMailService mailService;

    private final IOrgPathService orgPathService;

    private final IStageService stageService;

    @Resource(name = "pythonSpider")
    private IPythonSpider pythonSpider;

    private final MemberServiceImpl memberService;

    public MemberController(MemberServiceImpl memberService, IOrgPathService orgPathService, IStageService stageService, IMailService mailService) {
        this.memberService = memberService;
        this.orgPathService = orgPathService;
        this.stageService = stageService;
        this.mailService = mailService;
    }

    @ResponseBody
    @RequestMapping("/searchByFuzzyName")
    public List<Member> searchByFuzzyName(String fuzzy_name,HttpSession session){
        User currentUser=(User)session.getAttribute("USER_INFO");
        Integer uid=currentUser.getId();
        List<Member> memberList = memberService.searchListByFuzzyName(uid,fuzzy_name);
        return memberList;
    }

    /**
     * ???????????????????????????id????????????????????????member??????
     * @param userId ???????????????session.USER_INFO.id??????
     * @return ???????????????member??????
     * @throws Exception n
     * @ps ajax????????????????????????????????????model?????????????????????model???????????????fragment??????????????????????????????????????????????????????model??????
     */
    @RequestMapping("/getMembersJson")
    public ModelAndView getMemberJson(int userId, boolean isAsc, HttpSession session) throws Exception {

        User currentUser=(User)session.getAttribute("USER_INFO");
        Integer pathId=currentUser.getCurrent_path().getId();

        ModelAndView mv=new ModelAndView();

        final int IS_DELETE_STATUS=1;
        //??????member??????????????????????????????
        List<Member> memberList=memberService.selectMemberByUserIdAndPathId(userId,pathId,isAsc);
        List<Member> memberDeleteList=memberService.selectMemberByUserIdAndPathIdAndIsDelete(userId,pathId,isAsc,IS_DELETE_STATUS);

        //??????????????????
        Integer orgNums=memberList.size();


        //????????????times
        float timesAver=0;
        float totalTimes = 0;
        for (Member m:memberList) {
            totalTimes+=m.getTimes();
        }
        timesAver=totalTimes/orgNums;

        //?????????????????????
        Integer effectiveStatisticsStage=0;
        Integer maxTimes=0;
        for (int i = 0; i < memberList.size(); i++) {
            if(memberList.get(i).getTimes()>maxTimes){
                maxTimes=memberList.get(i).getTimes();
            }
        }
        effectiveStatisticsStage=maxTimes;


        //??????maxNumber
        currentUser.getCurrent_path().setMaxMemberNumber(orgNums);
        orgPathService.updateMaxNumberByPathId(orgNums,pathId);
        session.setAttribute("USER_INFO",currentUser);

        mv.addObject("MEMBER_LIST",memberList);
        mv.addObject("MEMBER_DELETE_LIST",memberDeleteList);
        mv.addObject("MEMBER_LIST_NUMBER",orgNums);
        mv.addObject("MEMBER_LIST_AverTimes",timesAver);
        mv.addObject("MEMBER_LIST_STATUS",isAsc);
        mv.addObject("MEMBER_LIST_MAXIMS",effectiveStatisticsStage);
        mv.setViewName("member-table::page_member_table");
        return mv;
    }

    /**
     * ???????????????Id???email???member????????????email.
     * @param memberId
     * @param email
     * @return  ?????????????????????0=false;1=true
     * @throws Exception n
     */
    @ResponseBody
    @RequestMapping("/updateMemberById")
    public int updateMemberById(Integer memberId, String email) throws Exception {
        int resultInt=memberService.updateOneWithId(memberId,email);
        return resultInt;
    }


    /**
     * ???????????????id??????member
     * @param memberId
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/delMemberById")
    public int delMemberById(Integer memberId,HttpSession session) throws Exception {
        int resultInt=memberService.deleteOneWithId(memberId);

        User currentUser=(User)session.getAttribute("USER_INFO");
        OrgPath orgPath=currentUser.getCurrent_path();
        Integer delBeforeValue = orgPath.getMaxMemberNumber();
        orgPath.setMaxMemberNumber(delBeforeValue-1);
        currentUser.setCurrent_path(orgPath);

        session.setAttribute("USER_INFO",currentUser);

        return resultInt;
    }

    /**
     * ???????????????id??????member
     * @param memberId
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/ResumeMemberById")
    public int ResumeMemberById(Integer memberId,HttpSession session) throws Exception {
        int resultInt=memberService.resumeOneWithId(memberId);

        User currentUser=(User)session.getAttribute("USER_INFO");
        OrgPath orgPath=currentUser.getCurrent_path();
        Integer delBeforeValue = orgPath.getMaxMemberNumber();
        orgPath.setMaxMemberNumber(delBeforeValue+1);
        currentUser.setCurrent_path(orgPath);

        session.setAttribute("USER_INFO",currentUser);

        return resultInt;
    }

    @RequestMapping("/addMemberManual")
    public String addMemberManual(@RequestParam(value = "name-input") String memberName,@RequestParam(value = "email-input") String memberEmail,
                               @RequestParam(value = "parentId-input")Integer parentId,
                               @RequestParam(value = "path-input") String path) throws Exception {
        int resultInt=memberService.addMemberByNameAndEmail(memberName,memberEmail,parentId,path);
        return "redirect:/memberTable";
    }

    @RequestMapping("/addMemberByPath")
    public String addMemberByPath(@RequestParam(value = "id") int id,@RequestParam(value = "orgPath") String orgPath,int maxStage,Integer pathId){

        pythonSpider.saveMemberOfOnePath(id,orgPath,maxStage,pathId);
        return "member-table";
    }

    @ResponseBody
    @RequestMapping("/sendRemindEmailToMember")
    public String sendRemindEmailToMember(HttpSession session,String memberId) throws MessagingException {
        User user = (User)session.getAttribute("USER_INFO");
        Member member = memberService.selectMemberById(Integer.parseInt(memberId));
        //??????????????????????????????
        if(member.getEmail()!=""||member.getEmail()!= null){
            mailService.sendThymeleafMail(user.getUserEmail().getTitle(),member.getName(),member.getEmail(),
                    user.getUserEmail().getName(),user.getEmail(),user.getUserEmail().getContent());
        }
        return "success";
    }
}
