package com.ll.youthlearn.controller;

import com.ll.youthlearn.entity.Member;
import com.ll.youthlearn.service.impl.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/2/16 22:11
 * @Description :
 */
@Controller
@Slf4j
@RequestMapping("/Member")
public class MemberController {

    private final MemberServiceImpl memberService;

    public MemberController(MemberServiceImpl memberService) {
        this.memberService = memberService;
    }

    /**
     * 根据传入的用户主键id，返回联动相关的member列表
     * @param userId 用户主键，session.USER_INFO.id提供
     * @return 联动相关的member列表
     * @throws Exception
     * @ps ajax调用时新生页面会调用不到model值，推测是因为model内的值都给fragment中的片段填充了。解决办法是多设置几个model变量
     */
    @RequestMapping("/getMembersJson")
    public ModelAndView getMemberJson(int userId, boolean isAsc) throws Exception {

        ModelAndView mv=new ModelAndView();

        //获取member列表，同时作为信息源
        List<Member> memberList=memberService.selectMemberByUserIdAndOrder(userId,isAsc);

        //获取组织人数
        Integer orgNums=memberList.size();

        //获取平均times
        float timesAver=0;
        float totalTimes = 0;
        for (Member m:memberList) {
            totalTimes+=m.getTimes();
        }
        timesAver=totalTimes/orgNums;



        mv.addObject("MEMBER_LIST",memberList);
        mv.addObject("MEMBER_LIST_NUMBER",orgNums);
        mv.addObject("MEMBER_LIST_AverTimes",timesAver);
        mv.addObject("MEMBER_LIST_STATUS",isAsc);
        mv.setViewName("member-table::page_member_table");
        return mv;
    }



    /**
     * 根据传入的【xx|xxx】格式的路径为条件，爬虫搜索相关的member并且其parent_user_id=传入的id
     * @Ps 注意：此方法只执行一次，正常情况是用户完成创建时触发！多次执行会导致times成倍增加。
     * @param id Session.USRR_INFO.id提供的用户主键；形如：1、2、3...
     * @param orgPath 特殊字符串：团委省|安徽艺术学院;一定要保证是全路径即可以按照这个路径找到学生【暂时】
     * @return
     */
    @RequestMapping("/addMemberByPath")
    public String addMemberByPath(@RequestParam(value = "id") int id,@RequestParam(value = "orgPath") String orgPath){
        log.info(id+" "+orgPath);

        //拼接param:xxx|xx->["name1","level2Name"]
        String orgPathStrParam="";
        String[] orgPath_TempSplitArray=orgPath.split("\\|");

        orgPathStrParam+="[";
        for (String s:orgPath_TempSplitArray) {
            orgPathStrParam+="'"+s+"',";
        }
        //去掉最后一个‘,'
        orgPathStrParam=orgPathStrParam.substring(0,orgPathStrParam.length()-1);
        orgPathStrParam+="]";
        //转换id参数
        String userStrId=String.valueOf(id);

        //TODO 做成工厂模式
        //调用python
        Process proc;
        String location = System.getProperty("user.dir") + "\\src\\main\\java\\com\\ll\\youthlearn\\python\\GetMemberByPathToMySQL.py";
        String[] params= new String[]{"python",location,userStrId,orgPathStrParam};
        try {
            String[] args=new String[]{"python", location,userStrId,orgPathStrParam};
            proc = Runtime.getRuntime().exec(args);
            //用输入输出流来截取结果
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(),"gbk"));
            String line = null;
            while ((line = in.readLine()) != null) {
                log.info(line);
            }
            in.close();
            proc.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "member-table";
    }
}
