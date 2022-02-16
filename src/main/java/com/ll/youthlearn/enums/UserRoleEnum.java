package com.ll.youthlearn.enums;

import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/2/15 14:10
 * @Description :
 */
@ToString
@AllArgsConstructor
public enum UserRoleEnum {
    user("user",1),admin("admin",2);

    private final String name;
    private final int index;
}
