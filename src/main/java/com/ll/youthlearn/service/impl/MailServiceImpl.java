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
