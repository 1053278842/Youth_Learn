package com.ll.youthlearn.service.impl;

import com.ll.youthlearn.utils.DateUtils;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/3/8 15:47
 * @Description :
 */
class StageServiceImplTest {
    @Test
    public void t(){
        Timestamp currentTs = new Timestamp(System.currentTimeMillis());

        String MonTs;
        String SunTs;

        MonTs= DateUtils.getWeekMondayDate("yyyy-MM-dd HH:mm:ss",currentTs);
        SunTs = DateUtils.getWeekSundayDate("yyyy-MM-dd HH:mm:ss",currentTs);
    }
}