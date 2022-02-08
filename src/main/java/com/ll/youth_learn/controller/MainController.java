package com.ll.youth_learn.controller;

import com.ll.youth_learn.entity.TopOrg;
import com.ll.youth_learn.mapper.TopOrgMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@Controller
public class MainController {

    @Autowired
    public TopOrgMapper topOrgMapper;

    @ResponseBody
    @RequestMapping("/getTopOrgList")
    public List<TopOrg> getTopOrgList(){
        return topOrgMapper.selectList(null);
    }

    @RequestMapping("/goIndex")
    public String goIndex(){
        log.warn("用户偷跑！");
        return "index";
    }
}
