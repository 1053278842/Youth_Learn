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
 * @CreateTime :  2022/2/9 1:25
 * @Description :
 */
@Data
@TableName("t_user")
public class User implements Serializable {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @TableField("username")
    private String username;

    @TableField("email")
    private String email;

    @TableField("password")
    private String password;

    /**存放用户所创建组织的路径信息**/
    @TableField("org_path")
    private String orgPath;

    @TableField("role")
    private String role;

    /**一对多的关系,存储该账户所管理下的所有路径以及路径信息**/
    @TableField(value="paths",exist = false)
    private List<OrgPath> paths;

    /**
     * 一个user当前只有一个OrgPath对象,默认是第一个即paths[0]
     */
    @TableField(value="current_path",exist = false)
    private OrgPath current_path;
}
