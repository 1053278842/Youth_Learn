package com.ll.youthlearn.controller;

import com.ll.youthlearn.entity.User;
import com.ll.youthlearn.enums.UserRoleEnum;
import com.ll.youthlearn.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

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
@RequestMapping("/User")
@Controller
@Slf4j
public class UserController {
    
    @Resource(name ="userService")
    private IUserService userService;

    @RequestMapping("/Register")
    public ModelAndView register(User user) throws Exception {
        ModelAndView mv=new ModelAndView();
        user.setRole(UserRoleEnum.user.name());
        try {
            userService.insertUser(user);
            mv.addObject("msg",user.getEmail()+"账号注册成功！");
            mv.setViewName("index");
            return mv;
        } catch (Exception e) {
            e.printStackTrace();
            mv.setViewName("index");
            mv.addObject("msg",user.getEmail()+"账号已被注册！");
            log.info(user.getEmail()+"账号已被注册！");
            return mv;
        }
    }

}
