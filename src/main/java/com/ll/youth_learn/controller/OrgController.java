package com.ll.youth_learn.controller;

import com.ll.youth_learn.service.IOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/2/9 1:34
 * @Description :
 */
@RequestMapping("/Org")
@Controller
public class OrgController {

    @Autowired
    private IOrgService orgService;

}
