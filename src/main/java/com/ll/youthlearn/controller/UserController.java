package com.ll.youthlearn.controller;

import com.ll.youthlearn.entity.Member;
import com.ll.youthlearn.entity.OrgPath;
import com.ll.youthlearn.entity.User;
import com.ll.youthlearn.enums.UserRoleEnum;
import com.ll.youthlearn.service.IMemberService;
import com.ll.youthlearn.service.IOrgPathService;
import com.ll.youthlearn.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author: Liu Long
 * @CreateTime: 2022/2/9 0:14
 * @Description: 主要职能分为：对User的创建。。。
 */
@Controller
@Slf4j
public class UserController {

    @Autowired
    public MemberController memberController;

    @Autowired
    public MemberEachStageController memberEachStageController;

    private final IMemberService memberService;
    private final IUserService userService;

    private final IOrgPathService orgPathService;

    public UserController(IOrgPathService orgPathService, IUserService userService, IMemberService memberService) {
        this.orgPathService = orgPathService;
        this.userService = userService;
        this.memberService = memberService;
    }

    @ResponseBody
    @RequestMapping("/User/switchPath")
    public String switchPath(HttpSession session,@RequestParam(value = "id") Integer orgPathId) throws Exception {
        User current_user=(User)session.getAttribute("USER_INFO");

        //更新Session
        OrgPath switchOrgPath=orgPathService.selectOneById(orgPathId);
        if(switchOrgPath==null) {return "404未找到对应的路径！";}

        Integer userId = current_user.getId();
        OrgPath current_OrgPath= switchOrgPath;
        Integer pathId=current_OrgPath.getId();

        //更新path
        current_user.setPaths(orgPathService.selectListById(userId));

        //TODO 不做持久化处理，设置为isExits=false
        //获取member列表，同时作为信息源
        List<Member> memberList=memberService.selectMemberByUserIdAndPathId(userId,pathId,false);
        //获取组织人数
        Integer orgNums=memberList.size();
        //将组织人数存储到相关表中：t_org_path::maxMemberNumber
        orgPathService.updateMaxNumberByPathId(orgNums,pathId);
        current_OrgPath.setMaxMemberNumber(orgNums);

        current_user.setCurrent_path(current_OrgPath);
        session.setAttribute("USER_INFO",current_user);

        return "success";
    }

    @RequestMapping("/User/delById")
    public ModelAndView getAllStageByUid(Integer pathId, HttpSession session){

        ModelAndView mv=new ModelAndView();

        orgPathService.delByPathId(pathId);

        User current_user=(User)session.getAttribute("USER_INFO");
        current_user.setPaths(orgPathService.selectListById(current_user.getId()));
        current_user.setCurrent_path(current_user.getPaths().get(0));
        session.setAttribute("USER_INFO",current_user);

        mv.setViewName("/index");
        return mv;
    }

    @RequestMapping("/User/addOrgPath")
    public ModelAndView addOrgPath(@RequestParam(value = "orgPath") String path, HttpSession session){

        ModelAndView mv=new ModelAndView();
        mv.setViewName("redirect:/index");

        User current_user=(User)session.getAttribute("USER_INFO");
        if(current_user==null){
            return mv;
        }

        OrgPath newOrgPath=new OrgPath();
        Integer id= current_user.getId();
        String orgPath=path;
        newOrgPath.setUserId(id);
        newOrgPath.setOrgPath(orgPath);
        newOrgPath.setMaxMemberNumber(0);

        try {
            orgPathService.insert(newOrgPath);
        } catch (Exception e) {
            log.warn("插入出错！");
        }

        List<OrgPath> orgPathList=orgPathService.selectListById(id);
        //更新session
        current_user.setPaths(orgPathList);
        for (OrgPath o:orgPathList) {
            if(o.getId().equals(newOrgPath.getId())){
                current_user.setCurrent_path(o);
            }
        }
        if(current_user.getCurrent_path()==null){
            current_user.setCurrent_path(current_user.getPaths().get(0));
        }
        session.setAttribute("USER_INFO",current_user);

        //
        memberController.addMemberByPath(id,orgPath,35,newOrgPath.getId());
        memberEachStageController.addMemberEachStage(session,35);
        return mv;
    }

    @RequestMapping("/User/Register")
    public ModelAndView register(User user) throws Exception {
        ModelAndView mv=new ModelAndView();
        user.setRole(UserRoleEnum.user.name());

        try {
            int insertStatus = userService.insertUser(user);
            log.info(user.getEmail()+"账号注册成功！");
            mv.addObject("msg",user.getEmail()+"账号注册成功！");
            mv.setViewName("login");
            return mv;
        } catch (Exception e) {
            e.printStackTrace();
            mv.setViewName("login");
            mv.addObject("msg",user.getEmail()+"账号已被注册！");
            log.info(user.getEmail()+"账号已被注册！");
            return mv;
        }
    }

}
