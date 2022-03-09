package com.ll.youthlearn.service;

import com.ll.youthlearn.entity.Stage;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.text.ParseException;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/3/8 15:48
 * @Description :
 */
@WebAppConfiguration
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
class IStageServiceTest {

    @Autowired
    private IStageService stageService;

    @Test
    void findNewestStage() throws ParseException {

        Stage stage = stageService.findNewestStage();
        System.out.println(stage);
    }
}