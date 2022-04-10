package com.ll.youthlearn.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ll.youthlearn.entity.*;
import com.ll.youthlearn.factory.IPythonSpider;
import com.ll.youthlearn.service.IMemberEachStageService;
import com.ll.youthlearn.service.IMemberService;
import com.ll.youthlearn.service.IOrgPathService;
import com.ll.youthlearn.service.IStageService;
import com.ll.youthlearn.utils.JsonRegularUtils;
import com.ll.youthlearn.utils.SpiderUtils;
import com.ll.youthlearn.utils.UrlRegularUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.*;

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
@RequestMapping("/MemberEachStage")
public class MemberEachStageController {

    @Resource(name = "pythonSpider")
    private IPythonSpider pythonSpider;

    private final IOrgPathService orgPathService;
    private final IMemberEachStageService memberEachStageService;
    private final IMemberService memberService;
    private final IStageService stageService;

    public MemberEachStageController(IMemberService memberService, IOrgPathService orgPathService, IMemberEachStageService memberEachStageService, IStageService stageService) {
        this.memberService = memberService;
        this.orgPathService = orgPathService;
        this.memberEachStageService = memberEachStageService;
        this.stageService = stageService;
    }


    @ResponseBody
    @RequestMapping("/getOneMemberEachStatus")
    public String getOneMemberEachStatus(Integer memberId) throws Exception {

        //升序数据
        List<MemberEachStage> mesList = memberEachStageService.selectListByMemberId(memberId);
        if(mesList.size()==0){
            return "";
        }

        List<Object> dataList=new ArrayList<>();
        int xAxis=0;
        int yAxis=0;

        //和前端对应的坐标参数、状态参数
        final int XMAXBOUND=20;
        final int STATUS_STUDY=-1;
        final int STATUS_NO_STUDY=0;
        final int STATUS_STUDY_OVERTIME=1;

        for(int i=0;i< mesList.size();i++){
            MemberEachStage mes=mesList.get(i);
            Integer studyStatus=STATUS_STUDY;
            //mes无数据即为无记录,相当于没有学习记录
            if(mes.getId()==0){
                studyStatus=STATUS_NO_STUDY;
            }else{
                //有记录时：判断该条记录的状态：未学习/学习/超时学习
                Calendar cal = Calendar.getInstance();
                cal.setFirstDayOfWeek(Calendar.MONDAY);

                //防止越界,忽略最后一个元素
                if(i<mesList.size()-1){
                    //获取当期后存在的&最近日期的一个mes对象
                    MemberEachStage mesAfterOneRow=null;
                    //不存在为空的情况
                    List<MemberEachStage> AfterList=mesList.subList(i+1,mesList.size());
                    
                    long minValueTime=new Timestamp(System.currentTimeMillis()).getTime();
                    for (int j = 0; j < AfterList .size(); j++) {
//                        if (j == AfterList .size() - 1) {
//                            continue;
//                        }
                        MemberEachStage tempMes=AfterList .get(j);
                        //判空
                        if(tempMes.getId()!=0){
                            long nextTime = tempMes.getTimestamp().getTime();
                            if (nextTime < minValueTime) {
                                minValueTime = nextTime;
                                mesAfterOneRow=tempMes;
                            }
                        }
                    }

                    //BUG 三期：A 完成 B 超时完成 C 刚开始,其中C是最新一期. 又或者用户超时某一期后,不曾继续学习其他期次. 此种情况下超时被系统判断为正常学习！
                    if(mesAfterOneRow!=null&&mesAfterOneRow.getId()!=0){

                        if(mes.getTimestamp().getTime()>mesAfterOneRow.getTimestamp().getTime()){
                            studyStatus=STATUS_STUDY_OVERTIME;
                        }else{
                            cal.setTime(mes.getTimestamp());
                            cal.setFirstDayOfWeek(Calendar.MONDAY);
                            int weekDay=cal.get(Calendar.WEEK_OF_YEAR);
                            cal.setTime(mesAfterOneRow.getTimestamp());
                            cal.setFirstDayOfWeek(Calendar.MONDAY);
                            int weekDayAfter=cal.get(Calendar.WEEK_OF_YEAR);
                            studyStatus=(weekDay==weekDayAfter?STATUS_STUDY_OVERTIME:STATUS_STUDY);
                        }
                    }
                }
            }

            Integer[] tempPoint = new Integer[]{xAxis,yAxis,studyStatus};
            xAxis++;
            if(xAxis>=XMAXBOUND){
                xAxis=0;
                yAxis++;
            }
            dataList.add(new Object[]{tempPoint,mes.getStage().getName()});
        }
        return JSON.toJSONString(dataList);
    }
    /**
     * 仅抓取当期青年大学习的数据
     * sidebar中跳转至member-current.html页面的跳转方法，会且必须传递Model数据
     * @param httpSession 获取当前用户信息用
     * @return 跳转到member-current.html
     */
    @RequestMapping("/goMemberCurrent")
    public ModelAndView goMemberCurrent(HttpSession httpSession) throws Exception {

        User user = (User)httpSession.getAttribute("USER_INFO");

        ModelAndView mv=new ModelAndView();
        mv.setViewName("member-current");

        //获取当期Stage对象
        Stage stage= stageService.findNewestStage();
        if(stage!=null){

            List<MemberEachStage> mesList=memberEachStageService.selectByOneOrg(stage.getId(),user.getId(),user.getCurrent_path().getId());

            //初始化日期结构s
            /*
            01 02 03 04 05 06
            11 12 13 14 15 16
            21 22 23 24 25 26
            31 32 33 34 35 36
            41 42 43 44 45 46
            51 52 53 54 55 56
            61 62 63 64 65 66
             */
            final int timeCount=6;
            final int weekDayCount=7;
            List<Integer[]> calendarList=new ArrayList();
            for (int week = 0; week < weekDayCount; week++) {
                for (int time = 0; time < timeCount; time++) {
                    calendarList.add(new Integer[]{week,time,0});
                }
            }
            for (MemberEachStage mes: mesList){
                Calendar cal = Calendar.getInstance();
                cal.setTime(mes.getTimestamp());

                //周次
                Integer hour=cal.get(Calendar.HOUR_OF_DAY);
                cal.setFirstDayOfWeek(Calendar.MONDAY);
                Integer weekDay=cal.get(Calendar.DAY_OF_WEEK);

                Double hourIndex=Math.floor(hour/(24/timeCount));
                //-1 -> 为了消除calendar sunday为每周第一天的误差; -1 -> 消除index下标和实际统计1的误差
                if(weekDay==1){
                    weekDay=6;
                }
                Integer weekDayIndex=weekDay-2;

                Integer localIndex=(hourIndex.intValue())+weekDayIndex*timeCount;
                calendarList.get(localIndex)[2]++;
            }
            String jsonDataStr=JSON.toJSONString(calendarList);

            //获取未参与学习的学生
            List<Member> memberList=memberService.selectMemberByUserIdAndPathId(user.getId(),user.getCurrent_path().getId(),false);
            HashMap<Integer,Member> membersMap=new HashMap<>(memberList.size());
            for (Member m: memberList ) {
                membersMap.put(m.getId(),m);
            }
            //和mesList的member比对,不存在于mes中的就是没学习的学生
            for (MemberEachStage mes: mesList ) {
                membersMap.remove(mes.getMember().getId());
            }
            List<Member> noStudyMemberList=new ArrayList<>();
            for (Map.Entry<Integer,Member> entry : membersMap.entrySet()) {
                noStudyMemberList.add(entry.getValue());
            }
            mv.addObject("MEMBER_LIST",mesList);
            mv.addObject("NO_STUDY_MEMBER_LIST",noStudyMemberList);
            mv.addObject("CALENDAR_JSON", jsonDataStr);
        }else{
            //TODO 提示没有当前期
        }

        return mv;
    }


    @ResponseBody
    @RequestMapping("/addMemberEachStage")
    public String addMemberEachStage(HttpSession session,Integer maxStage){

        //判断当前时间下，是否存在最新的期次，即是否属于寒暑假放假期间
        Stage stage=stageService.findNewestStage();
        Integer inNewStage=0;
        if(stage==null){
            inNewStage=0;
        }


        //TODO BUG:maxStage指定不准，如果存在最新stage,则总显示条数会-1,修改为指定范围
        Integer id = ((User)session.getAttribute("USER_INFO")).getId();
        Integer pathId = ((User)session.getAttribute("USER_INFO")).getCurrent_path().getId();
        String orgPath=((User)session.getAttribute("USER_INFO")).getCurrent_path().getOrgPath();

        //删除多余的memberEachStage数据,只在maxStage>MySQL:maxStage情况下发生
        memberEachStageService.deleteMemberEachStageByUserIdAndMaxStage(id,pathId,maxStage);

        pythonSpider.saveMemberEachStage(id,orgPath,maxStage,pathId,inNewStage);

        return "";
    }

    /**
     * 该接口自动执行
     * @return
     */
    @ResponseBody
    @RequestMapping("/getCurrentStageMember")
    public String getCurrentStageMember() throws IOException {
        pythonSpider.saveAllStage();
        //判断当前时间下，是否存在最新的期次，即是否属于寒暑假放假期间
        Stage stage=stageService.findNewestStage();
        if(stage!=null){

            //获取org_top列
            List<OrgPath> orgPathList=orgPathService.selectList();

            //去除orgPath相同的记录,防止重复爬取,造成性能损失
            HashMap<String,OrgPath> hashMap=new HashMap(orgPathList.size());
            for (OrgPath op: orgPathList) {
                hashMap.put(op.getOrgPath(),op);
            }
            List<OrgPath> orgPathListNoRepeat=new ArrayList<OrgPath>();
            for (OrgPath op: hashMap.values()) {
                orgPathListNoRepeat.add(op);
            }

            //存储需要插入的对象,只为批量插入
            List<MemberEachStage> mesList=new ArrayList<MemberEachStage>();

            //存储爬虫爬取的数据集
            HashMap<String,String> spiderResultsHasMap=new HashMap(orgPathListNoRepeat.size());
            //根据OrgPath列表进行爬虫抓取,抓到member_each_stage表中
            //TODO 使用多线程
            for (OrgPath op : orgPathListNoRepeat ) {

                //爬虫Start
                String url = "dxx.ahyouth.org.cn/api/peopleRankStage";
                url = UrlRegularUtils.setRequestProtocolHeader(url, false);
                url = UrlRegularUtils.setStage(url, stage.getStage());
                url = UrlRegularUtils.setLevelParams(url, op.getOrgPath());

                String jsonData = SpiderUtils.getHttpJson(url);

                spiderResultsHasMap.put(op.getOrgPath(), jsonData);
                //爬虫End
            }
            for(HashMap.Entry<String, String> entry : spiderResultsHasMap.entrySet()){
                String currentOrgPathStr=entry.getKey();
                String jsonData=entry.getValue();


                //对爬到的数据进行处理
                if(jsonData!=null){

                    //转换成json数据处理
                    JSONArray jsonArray= JsonRegularUtils.getMemberListByJsonData(jsonData);

                    //获取当前String型路径下的pathId.userId的集合.实现相同路径组织的不同人员关系插入！
                    List<OrgPath> orgPathRlatList = orgPathService.selectListByOrgPathStr(currentOrgPathStr);

                    //循环遍历其他组织
                    for (OrgPath op:orgPathRlatList) {
                        //打印
                        System.out.println(MessageFormat.format("当前组织：{0},当前期次：{1},爬取到成员数量：{2}",
                                op.getOrgPath(),stage.getName(),jsonArray.size()));

                        //插入和检索需要的主键Id
                        Integer stageId = stage.getId();
                        Integer userId = op.getUserId();
                        Integer pathId = op.getId();
                        String orgPath=op.getOrgPath();

                        //获取memberList,作为限制的依据
                        List<Member> memberList = memberService.selectByPathIdIgnoreIsDelete(pathId);
                        HashMap<String,Member> memberHashMap=new HashMap<>();
                        for (Member member:memberList) {
                            memberHashMap.put(member.getName(),member);
                        }

                        //插入新用户和新member_each_stage
                        for (int i = 0; i < jsonArray.size(); i++) {
                            Member member = memberHashMap.get((String)jsonArray.getJSONObject(i).get("username"));
                            //json数据中的用户不在member表中的情况
                            if(member==null){
                                //TODO 单条插入改写成批量插入,提升速度
                                //爬取到的用户不存在于member表中（包括isDelete标记的member）,是新的用户。进行插入新用户操作
                                Member newMember=new Member();
                                newMember.setName((String)jsonArray.getJSONObject(i).get("username"));
                                newMember.setPath(orgPath);
                                newMember.setPathId(pathId);
                                newMember.setParentUserId(userId);
                                memberService.insertOne(newMember);

                                System.out.println(MessageFormat.format("新插入用户：{0},路径{1}",(String)jsonArray.getJSONObject(i).get("username"),orgPath));
                                continue;
                            }

                            Integer memberId=memberHashMap.get(jsonArray.getJSONObject(i).get("username")).getId();
                            //member_stage_each存储
                            MemberEachStage memberEachStage =new MemberEachStage();
                            memberEachStage.setMember_id(memberId);
                            memberEachStage.setStage_id(stageId);
                            memberEachStage.setUser_id(userId);
                            memberEachStage.setTimestamp(null);

                            mesList.add(memberEachStage);
                            System.out.println("memberId");
                        }
                    }
                }
            }
            //批量插入
            if(mesList.size()!=0){
                memberEachStageService.insertMany(mesList);
            }
        }else{
            System.out.println("未找到最新期次,更新最新期次中断");
        }
        return "success";
    }
}
