package com.ll.youthlearn.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/3/24 23:28
 * @Description :
 */

@Data
@TableName("t_user_email")
public class UserEmail implements Serializable {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @TableField("user_id")
    private Integer userId;

    @TableField("name")
    private String name;

    @TableField("email")
    private String email;

    @TableField("content")
    private String content;

    /**五天后自动提醒未学习人员的开关**/
    @TableField("auto_remind")
    private Integer autoRemind;

    /**每期开始时自动提醒未学习人员的开关**/
    @TableField("auto_remind_start")
    private Integer autoRemindStart;

    @TableField("title")
    private String title;

    /**
     * userEmail和member的一对多关系
     */
    @TableField("members")
    private List<Member> members;
}