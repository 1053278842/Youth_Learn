package com.ll.youthlearn.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/2/25 12:52
 * @Description :
 */
@Data
@TableName("t_stage")
public class Stage implements Serializable {

    @TableId(value = "id",type= IdType.AUTO)
    private int id;

    @TableField("name")
    private String name;

    @TableField("stage")
    private String stage;

    @TableField("stage_date")
    private Timestamp stageDateInsertTime;

    @TableField(value = "members",exist = false)
    List<MemberEachStage> members;

    @TableField(value = "stageDate",exist = false)
    String stageDate;
}
