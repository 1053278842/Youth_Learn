package com.ll.youthlearn.config;

import com.ll.youthlearn.entity.Member;
import com.ll.youthlearn.entity.Stage;
import com.ll.youthlearn.entity.UserEmail;
import com.ll.youthlearn.factory.IPythonSpider;
import com.ll.youthlearn.mapper.IStageMapper;
import com.ll.youthlearn.service.*;
import com.ll.youthlearn.utils.DateUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.Calendar;
import java.util.List;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/3/28 2:21
 * @Description :
 */
@WebAppConfiguration
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
class StaticScheduleTaskTest {

    @Resource(name = "pythonSpider")
    private IPythonSpider pythonSpider;

    @Autowired
    private  IOrgPathService orgPathService;
    @Autowired
    private  IMemberEachStageService memberEachStageService;
    @Autowired
    private  IMemberService memberService;
    @Autowired
    private  IStageService stageService;
    @Autowired
    private IStageMapper stageMapper;
    @Autowired
    private  IMailService mailService;


    @Test
    public void currentStageSearch() throws MessagingException {
        Stage stage=null;
        stage=stageMapper.selectById(4619);
        if(stage!=null){
            //获取当前时间和当期stage对象的时间插值
            double day= DateUtils.getBetweenDate(stage.getStageDateInsertTime(), Calendar.getInstance().getTime());

            //当期开始不到一天
            if(day<=1){
                List<UserEmail>  userEmailList= memberService.selectEmailAutoRemindWeekStart(stage.getId());
                for (UserEmail userEmail:userEmailList) {
                    for (Member m: userEmail.getMembers()) {
                        mailService.sendThymeleafMail("青年大学习提醒",m.getName(),m.getEmail(), userEmail.getName(), userEmail.getEmail(),
                                "该青年大学习啦！");
                    }
                }
            }

            //当期开时后已经过去了五天
            if(day>=5 && day<6 ){
                List<UserEmail>  userEmailList= memberService.selectEmailAutoRemindWeekStart(stage.getId());
                for (UserEmail userEmail:userEmailList) {
                    for (Member m: userEmail.getMembers()) {
                        mailService.sendThymeleafMail(userEmail.getTitle(), m.getName(),m.getEmail(), userEmail.getName(), userEmail.getEmail(),
                                userEmail.getContent());
                    }
                }
            }
        }
    }
}