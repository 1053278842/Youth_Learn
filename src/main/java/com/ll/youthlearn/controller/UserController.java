package com.ll.youthlearn.controller;

import com.ll.youthlearn.entity.OrgPath;
import com.ll.youthlearn.entity.User;
import com.ll.youthlearn.enums.UserRoleEnum;
import com.ll.youthlearn.service.IOrgPathService;
import com.ll.youthlearn.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

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
    
    private final IUserService userService;

    private final IOrgPathService orgPathService;

    public UserController(IOrgPathService orgPathService, IUserService userService) {
        this.orgPathService = orgPathService;
        this.userService = userService;
    }

    @ResponseBody
    @RequestMapping("/User/switchPath")
    public String switchPath(HttpSession session,@RequestParam(value = "id") Integer orgPathId){

        User current_user=(User)session.getAttribute("USER_INFO");
        current_user.setCurrent_path(orgPathService.selectOneById(orgPathId));
        session.setAttribute("USER_INFO",current_user);
        return "success";
    }

    @RequestMapping("/User/addOrgPath")
    public ModelAndView addOrgPath(User user, HttpSession session){
        ModelAndView mv=new ModelAndView();
        mv.setViewName("redirect:/index");

        User current_user=(User)session.getAttribute("USER_INFO");
        if(current_user==null){
            return mv;
        }

        OrgPath newOrgPath=new OrgPath();
        Integer id= current_user.getId();
        String orgPath=user.getOrgPath();
        newOrgPath.setUserId(id);
        newOrgPath.setOrgPath(orgPath);
        newOrgPath.setMaxMemberNumber(0);

        log.warn(newOrgPath.toString());
        try {
            orgPathService.insert(newOrgPath);
        } catch (Exception e) {
            log.warn("插入出错！");
        }

        //更新session
        current_user.setPaths(orgPathService.selectListById(id));
        current_user.setCurrent_path(current_user.getPaths().get(0));
        session.setAttribute("USER_INFO",current_user);
        return mv;
    }

    @RequestMapping("/User/Register")
    public ModelAndView register(User user) throws Exception {
        ModelAndView mv=new ModelAndView();
        user.setRole(UserRoleEnum.user.name());

        try {
            userService.insertUser(user);
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
