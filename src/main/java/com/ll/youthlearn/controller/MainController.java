package com.ll.youthlearn.controller;

import com.ll.youthlearn.mapper.ITopOrgMapper;
import com.ll.youthlearn.service.impl.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author ll
 */
@Slf4j
@Controller
public class MainController {

    private final MemberServiceImpl memberService;

    public final ITopOrgMapper topOrgMapper;

    public MainController(MemberServiceImpl memberService, ITopOrgMapper topOrgMapper) {
        this.memberService = memberService;
        this.topOrgMapper = topOrgMapper;
    }

    @RequestMapping("/index")
    public String index(){
        return "index";
    }
    @RequestMapping("/register")
    public String register(){
        return "register";
    }
    @RequestMapping("/login")
    public String login(){
        return "login";
    }
    @RequestMapping("/memberTable")
    public String memberTable() {
        return "member-table";
    }
    @RequestMapping("/memberLast")
    public String memberLast() {
        return "member-last";
    }
}
