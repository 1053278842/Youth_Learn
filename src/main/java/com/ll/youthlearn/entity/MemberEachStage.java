package com.ll.youthlearn.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/2/25 15:37
 * @Description :
 */
@Data
@TableName("t_member_each_stage")
public class MemberEachStage implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    private int id;

    @TableField("member_id")
    private int member_id;

    @TableField("timestamp")
    private Timestamp timestamp;

    @TableField("stage_id")
    private int stage_id;

    @TableField("user_id")
    private int user_id;

    @TableField(value = "stage",exist = false)
    private Stage stage;

    @TableField(value = "memberName",exist = false)
    private String memberName;
}
