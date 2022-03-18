package com.ll.youthlearn.controller;

import com.ll.youthlearn.factory.IPythonSpider;
import com.ll.youthlearn.service.IMemberEachStageService;
import com.ll.youthlearn.service.IMemberService;
import com.ll.youthlearn.service.IOrgPathService;
import com.ll.youthlearn.service.IStageService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/3/9 13:20
 * @Description :
 */
@WebAppConfiguration
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
class MemberEachStageControllerTest {

    @Autowired
    private IStageService stageService;
    @Autowired
    private IOrgPathService orgPathService;
    @Autowired
    private IMemberService memberService;
    @Autowired
    private IMemberEachStageService memberEachStageService;

    @Resource(name = "pythonSpider")
    private IPythonSpider pythonSpider;

    @Test
    void getCurrentStageMember() throws IOException {

    }
}