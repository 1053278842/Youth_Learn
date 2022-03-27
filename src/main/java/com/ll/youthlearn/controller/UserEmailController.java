package com.ll.youthlearn.controller;

import com.ll.youthlearn.entity.User;
import com.ll.youthlearn.entity.UserEmail;
import com.ll.youthlearn.service.IUserEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/3/25 1:42
 * @Description :
 */
@Controller
@Slf4j
@RequestMapping("/user-email")
public class UserEmailController {

    private final IUserEmailService userEmailService;

    public UserEmailController(IUserEmailService userEmailService) {
        this.userEmailService = userEmailService;
    }

    @RequestMapping("/updateEmailInfo")
    public ModelAndView updateEmailInfo(HttpSession session,UserEmail userEmail){

        ModelAndView mv=new ModelAndView();
        mv.setViewName("email-edit");

        userEmailService.updateOne(userEmail);

        //修改当前用户的USER_INFO,统一一下
        User user = (User)session.getAttribute("USER_INFO");
        UserEmail tempUserEmail= user.getUserEmail();
        tempUserEmail.setContent(userEmail.getContent());
        tempUserEmail.setTitle(userEmail.getTitle());
        tempUserEmail.setName(userEmail.getName());
        tempUserEmail.setAutoRemind(userEmail.getAutoRemind());
        user.setUserEmail(tempUserEmail);
        session.setAttribute("USER_INFO",user);

        return mv;
    }
}
