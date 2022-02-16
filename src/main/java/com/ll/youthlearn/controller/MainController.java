package com.ll.youthlearn.controller;

import com.ll.youthlearn.entity.TopOrg;
import com.ll.youthlearn.mapper.ITopOrgMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author ll
 */
@Slf4j
@Controller
public class MainController {

    public final ITopOrgMapper topOrgMapper;

    public MainController(ITopOrgMapper topOrgMapper) {
        this.topOrgMapper = topOrgMapper;
    }

    @ResponseBody
    @RequestMapping("/getTopOrgList")
    public List<TopOrg> getTopOrgList(){
        return topOrgMapper.selectList(null);
    }

    @RequestMapping("/goIndex")
    public String goIndex(){
        return "index";
    }

    @RequestMapping("/JavaSpider")
    public String JavaSpider(){
        //打开浏览器
        CloseableHttpClient httpClient= HttpClients.createDefault();
        CloseableHttpResponse response = null;
        //创建get请求
//        HttpGet request = new HttpGet()
        return "index";
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
}
