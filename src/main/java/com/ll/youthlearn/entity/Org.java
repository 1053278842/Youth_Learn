package com.ll.youthlearn.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/2/9 1:22
 * @Description :
 */
@TableName("t_org")
@Data
public class Org implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField("name")
    private String name;
    @TableField("path")
    private String path;
    @TableField("parent_id")
    private Integer parentId;
}
