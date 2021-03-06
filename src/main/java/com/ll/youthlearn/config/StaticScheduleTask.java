package com.ll.youthlearn.config;

import com.alibaba.fastjson.JSONArray;
import com.ll.youthlearn.entity.*;
import com.ll.youthlearn.factory.IPythonSpider;
import com.ll.youthlearn.service.*;
import com.ll.youthlearn.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/3/17 0:55
 * @Description :
 */
@Configuration
@EnableScheduling
@Slf4j
public class StaticScheduleTask {

    @Resource(name = "pythonSpider")
    private IPythonSpider pythonSpider;

    private final IOrgPathService orgPathService;
    private final IMemberEachStageService memberEachStageService;
    private final IMemberService memberService;
    private final IStageService stageService;
    private final IMailService mailService;

    public StaticScheduleTask(IMemberService memberService, IOrgPathService orgPathService, IMemberEachStageService memberEachStageService, IStageService stageService, IMailService mailService) {
        this.memberService = memberService;
        this.orgPathService = orgPathService;
        this.memberEachStageService = memberEachStageService;
        this.stageService = stageService;
        this.mailService = mailService;
    }

    @Scheduled(cron = "0 0 0/4 ? * ?  ")
    private void catchCurrentStageMemberEachStage() throws UnsupportedEncodingException {

        log.info("每4小时一次的数据爬取开始！");

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
//                        System.out.println(MessageFormat.format("当前组织：{0},当前期次：{1},爬取到成员数量：{2}",
//                                op.getOrgPath(),stage.getName(),jsonArray.size()));

                        //插入和检索需要的主键Id
                        Integer stageId = stage.getId();
                        Integer userId = op.getUserId();
                        Integer pathId = op.getId();
                        String orgPath=op.getOrgPath();
                        //xxx|xxx|xx型转化为['XXX','XXX','xx']
                        orgPath= PythonUtils.RegulatePath(orgPath);

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

                                log.info(MessageFormat.format("新插入用户：{0},路径{1}",(String)jsonArray.getJSONObject(i).get("username"),orgPath));
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
                        }
                    }
                }
            }
            //批量插入
            if(mesList.size()!=0){
                memberEachStageService.insertMany(mesList);
            }
        }else{
            log.warn("未找到最新期次,更新最新期次中断");
        }
    }

    /**
     * 该方法用于判断
     * 1、是否有当前期
     * 2、判断当前时间和当前期隔了多少天
     * 从而根据当前和当前期的情况执行【期初提醒】和【五天后提醒】服务
     * “每天14:00,每天一次执行”
     */
    @Scheduled(cron = "0 0 14 1/1 * ?  ")
    private void currentStageSearch() throws MessagingException {
        final int LONG_TIME_REMIND_DAY=5;
        final int WEEK_START_REMIND_DAY=1;
        Stage stage=stageService.findNewestStage();
        if(stage!=null){
            //获取当前时间和当期stage对象的时间插值
            double day= DateUtils.getBetweenDate(stage.getStageDateInsertTime(), Calendar.getInstance().getTime());

            //当期开始不到一天
            if(day<=WEEK_START_REMIND_DAY){
                List<UserEmail>  userEmailList= memberService.selectEmailAutoRemindWeekStart(stage.getId());
                for (UserEmail userEmail:userEmailList) {
                    for (Member m: userEmail.getMembers()) {
                        mailService.sendThymeleafMail("青年大学习提醒",m.getName(),m.getEmail(), userEmail.getName(), userEmail.getEmail(),
                                "该青年大学习啦！");
                    }
                }
            }

            //当期开时后已经过去了五天
            if(day>=LONG_TIME_REMIND_DAY && day<LONG_TIME_REMIND_DAY+1 ){
                List<UserEmail>  userEmailList= memberService.selectEmailAutoRemindWeekStart(stage.getId());
                for (UserEmail userEmail:userEmailList) {
                    for (Member m: userEmail.getMembers()) {
                        mailService.sendThymeleafMail(userEmail.getTitle(), m.getName(),m.getEmail(), userEmail.getName(), userEmail.getEmail(),
                                userEmail.getContent());
                    }
                }
            }
        }
    }
}
