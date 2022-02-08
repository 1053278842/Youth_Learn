package com.ll.youth_learn.controller;

import com.ll.youth_learn.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
public class UserController {

    @Autowired
    private IUserService userService;

}
