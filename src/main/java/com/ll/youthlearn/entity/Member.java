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
 * @CreateTime :  2022/2/16 18:58
 * @Description :
 */
@Data
@TableName("t_member")
public class Member implements Serializable {

    @TableId(value="id",type = IdType.AUTO)
    private Integer id;

    @TableField("name")
    private String name;

    @TableField("email")
    private String email;

    @TableField("timestamp")
    private Timestamp timestamp;

    /**改为使用复杂sql赋值**/
    @TableField(value = "times",exist = false)
    private int times;

    @TableField("path")
    private String path;

    @TableField("path_id")
    private Integer pathId;

    @TableField("parent_user_id")
    private Integer parentUserId;

    /**
     * 0->假，非0->真。为0时正常显示，非0时为逻辑删除
     */
    @TableField("isDelete")
    private Integer isDelete;

    /**
     * 分组统计用的字段，意义模糊。多代表分组后的计数字段
     */
    @TableField(value = "countId",exist = false)
    private Integer countId;

}
