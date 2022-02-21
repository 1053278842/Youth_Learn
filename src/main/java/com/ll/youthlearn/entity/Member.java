package com.ll.youthlearn.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;
/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/2/16 18:58
 * @Description :
 */
@Data
@TableName("t_member")
public class Member {

    @TableId(value="id",type = IdType.AUTO)
    private Integer id;

    @TableField("name")
    private String name;

    @TableField("email")
    private String email;

    @TableField("timestamp")
    private Timestamp timestamp;

    @TableField("times")
    private Integer times;

    @TableField("path")
    private String path;

    @TableField("parent_user_id")
    private Integer parentUserId;

    @TableField("maxTimes")
    private Integer maxTimes;

}
