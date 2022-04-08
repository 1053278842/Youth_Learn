package com.ll.youthlearn.service.impl;

import com.ll.youthlearn.entity.Member;
import com.ll.youthlearn.entity.OrgRanking;
import com.ll.youthlearn.service.IMemberService;
import com.ll.youthlearn.service.IOrgRankingService;
import com.ll.youthlearn.utils.PythonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author: Liu Long
 * @CreateTime: 2022/2/9 0:31
 * @Description:
 */
@Slf4j
@Service("orgRankingService")
@Transactional(rollbackFor = Exception.class)
public class OrgRankingServiceImpl implements IOrgRankingService {

    private final IMemberService memberService;

    public OrgRankingServiceImpl(IMemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public List<OrgRanking> findCurrentStageRanking(Integer uid){
        List<OrgRanking> orgRankingList=new ArrayList<>();

        //获取每个组织下的平均完成人数
        Map<Integer,OrgRanking> currentStageRankMap = findOrgRanking(uid,0,1);

        Map<Integer,OrgRanking> beforeStageRankMap = findOrgRanking(uid,1,2);

        Map<Integer,OrgRanking> resultRankMap = setRankingTrend(currentStageRankMap,beforeStageRankMap);

        resultRankMap.forEach((key,value)->{
            orgRankingList.add(value);
        });
        return orgRankingList;
    }

    @Override
    public List<OrgRanking> findRangeStageRanking(Integer uid, Integer startIndex, Integer endNums) {
        List<OrgRanking> orgRankingList=new ArrayList<>();

        //获取每个组织下的平均完成人数
        Map<Integer,OrgRanking> resultRankMap = findOrgRanking(uid,startIndex,endNums);

        resultRankMap.forEach((key,value)->{
            orgRankingList.add(value);
        });
        return orgRankingList;
    }


    public Map<Integer,OrgRanking> findOrgRanking(Integer uid,Integer startStageIndex,Integer endStageNum){

        //获取每个组织下的平均完成人数
        List<Member> mList = memberService.findAvgOfGroupByPathCount(uid,startStageIndex,endStageNum);
        //获取member以及对应的总数
        List<Member> memberList = memberService.findCountOfGroupByPathId(uid);
        Map<Integer,Double> memberCountMap=new HashMap<Integer,Double>();
        for (Member m:memberList) {
            memberCountMap.put(m.getPathId(),m.getCount());
        }


        //转化为数组进行冒泡排序
        Member[] reachRateAvgMembers = new Member[mList.size()];
        int indexAvgMembers=0;

        //计算出每个组织的平均完成率
        Map<Integer,Double> memberReachRateAvgMap = new HashMap<Integer,Double>(mList.size());
        for (Member m:mList) {
            Integer pathId=m.getPathId();
            memberReachRateAvgMap.put(pathId,m.getCount()/memberCountMap.get(pathId));
//            log.info(pathId+" | "+memberReachRateAvgMap.get(pathId));

            //将count字段由原来的计数用改为记录平均达成率
            m.setCount(m.getCount()/memberCountMap.get(pathId));
            reachRateAvgMembers[indexAvgMembers]=m;
            indexAvgMembers++;
        }

        //冒泡
        for (int i=0;i<reachRateAvgMembers.length-1;i++){
            for (int j=0;j<reachRateAvgMembers.length-1-i;j++){
                if(reachRateAvgMembers[j].getCount()<reachRateAvgMembers[j+1].getCount()){
                    Member temp=reachRateAvgMembers[j];
                    reachRateAvgMembers[j]=reachRateAvgMembers[j+1];
                    reachRateAvgMembers[j+1]=temp;
                }else if(reachRateAvgMembers[j].getCount().equals(reachRateAvgMembers[j+1].getCount())){
                    if(reachRateAvgMembers[j].getPathId()>reachRateAvgMembers[j+1].getPathId()) {
                        Member temp = reachRateAvgMembers[j];
                        reachRateAvgMembers[j] = reachRateAvgMembers[j + 1];
                        reachRateAvgMembers[j + 1] = temp;
                    }
                }
            }
        }

        //输出
        Map<Integer,OrgRanking> orgRankingPathIdMap = new LinkedHashMap<>(reachRateAvgMembers.length-1);
        for (int i = 0; i < reachRateAvgMembers.length-1; i++) {
            Member m=reachRateAvgMembers[i];
            OrgRanking orgRankingTemp=new OrgRanking();
            orgRankingTemp.setRanking(i+1);
            orgRankingTemp.setOrgLastName(PythonUtils.ExtractLastOrgNameFromArrayStr(m.getPath()));
            orgRankingTemp.setPathId(m.getPathId());
            orgRankingTemp.setCompletionRate(memberReachRateAvgMap.get(m.getPathId()));
            orgRankingTemp.setOrgNumSize(memberCountMap.get(m.getPathId()).intValue());
            orgRankingTemp.setRankingTrend(0);

            orgRankingPathIdMap.put(orgRankingTemp.getPathId(),orgRankingTemp);
        }
        return orgRankingPathIdMap;
    }

    public Map<Integer, OrgRanking> setRankingTrend(Map<Integer,OrgRanking> currentStageRankMap, Map<Integer,OrgRanking> beforeStageRankMap ){
        currentStageRankMap.forEach((key,value)->{
            //如果前一期没有该组织的情况下,趋势等于默认值0
            if(beforeStageRankMap.containsKey(key)){
                Integer beforeRanking=beforeStageRankMap.get(key).getRanking();
                Integer currentRanking=value.getRanking();

                Integer rankingTrend=-currentRanking+beforeRanking;
                value.setRankingTrend(rankingTrend);
            }
        });
        return currentStageRankMap;
    }

}
