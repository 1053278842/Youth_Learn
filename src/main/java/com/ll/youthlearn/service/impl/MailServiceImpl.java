package com.ll.youthlearn.service.impl;

import com.ll.youthlearn.service.IMailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/3/23 14:27
 * @Description :
 */
@Transactional(rollbackFor=Exception.class)
@Service()
@Slf4j
public class MailServiceImpl implements IMailService {

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    TemplateEngine templateEngine;

    @Override
    public void sendThymeleafMail(String title,String toName,String toEmail,String fromName,String fromEmail,String content) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);

        //默认值处理
        title=title.equals("")?"青年大学习提醒":title;
        content=content.equals("")?"我敬爱的同志,近日庐州的天见不到太阳,连月亮也都是残缺的,不知同志您在那里呆的可习惯。不知同志可曾记得当初咱们在“青年大学习”下的誓盟,哪怕十二月的狂风也不曾让我感到寒冷,可是您的一丝疏忽让却我如坠冰窖,心底拔凉拔凉的不是滋味.同志,您不要再忘了咱们“青年大学习”的约定哦！":content;

        helper.setSubject(title);
        helper.setFrom("1053278842@qq.com");
        helper.setTo(toEmail);
        helper.setSentDate(new Date());
        //引入TemplateContext
        Context context =new Context();
        context.setVariable("member_name",toName);
        context.setVariable("myself_name",fromName);
        context.setVariable("myself_email",fromEmail);
        context.setVariable("content",content);

        //将context渲染到模板中
        String process=templateEngine.process("email-remind-template.html",context);
        //插入到邮件中,True表示这个是Html文本
        helper.setText(process,true);

        javaMailSender.send(mimeMessage);
    }
}
