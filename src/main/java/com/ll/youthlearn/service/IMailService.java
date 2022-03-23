package com.ll.youthlearn.service;

import javax.mail.MessagingException;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/3/23 14:25
 * @Description :
 */
public interface IMailService {
    /**
     *  使用thymeleaf的模板发送HTML邮件
     * @param title         邮件标题，为空有默认值
     * @param toName        内容中需要提到的，收件者的名字
     * @param toEmail       收件者的邮件地址
     * @param fromName      提醒者/发件者的名字
     * @param fromEmail     提醒者/发件者的邮箱地址
     * @param content       提醒者/发件者自我编辑的邮件内,为空有默认值
     * @throws MessagingException
     */
    void sendThymeleafMail(String title,String toName,String toEmail,String fromName,String fromEmail,String content) throws MessagingException;
}
