package com.ll.youthlearn.service.impl;

import com.ll.youthlearn.entity.Member;
import com.ll.youthlearn.entity.OrgRanking;
import com.ll.youthlearn.mapper.IOrgRankingMapper;
import com.ll.youthlearn.mapper.IStageMapper;
import com.ll.youthlearn.service.IMemberService;
import com.ll.youthlearn.service.IOrgRankingService;
import com.ll.youthlearn.utils.PythonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final IOrgRankingMapper orgRankingMapper;
    @Autowired
    private IStageMapper stageMapper;

    public OrgRankingServiceImpl(IMemberService memberService, IOrgRankingMapper orgRankingMapper) {
        this.memberService = memberService;
        this.orgRankingMapper = orgRankingMapper;
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

    @Override
    public List<Object[]> getEchartsLineRankingDate(Integer uid, Integer stageTotal) {

        //每个组织id和其未删除总成员的键值对
        Map<Integer,Double> memberCountMap=getTotalNumOfMemberGroupByPathMap(uid);

        List<OrgRanking> sourceOrgRankingList = orgRankingMapper.selectTotalNumberOfLearnersGroupedByStage(uid,stageTotal);

        //pathId ： pathName的Map
        Map<Integer,String> pathIdToNameMap = new HashMap<>();

        // 记录转储 pathId:TotalNums/MaxNums[stageTotal] 键值对
        Map<Integer,Double[]> pathIdMappingTotalNumsArrayMap = new HashMap<>();

        // 将数据源中的期次stageId按照不重复的先后顺序对应出数组下标,数组最大长度即为stageTotal
        // Object[0]-> 下标, Object[1]->stageName
        Map<Integer,Object[]> stageIdMappingIndexMap = new LinkedHashMap<>(stageTotal);

        Integer index_temp=0;
        for (OrgRanking or:sourceOrgRankingList ) {

            pathIdToNameMap.put(or.getPathId(),or.getPathName());

            Integer sid=or.getStageId();
            //下标存储
            if(!stageIdMappingIndexMap.containsKey(sid)){
                Object[] temps=new Object[2];
                temps[0]=index_temp;
                temps[1]=or.getStageName();
                stageIdMappingIndexMap.put(sid,temps);
                index_temp++;
            }

            //row存储并记录count值
            Integer pid=or.getPathId();
            Double[] avg_temps = null;

            if(!pathIdMappingTotalNumsArrayMap.containsKey(pid)){
                //创建初始化
                avg_temps = new Double[stageTotal];
            }else{
                //检出
                avg_temps = pathIdMappingTotalNumsArrayMap.get(pid);
            }
            Integer indexKey = (Integer) stageIdMappingIndexMap.get(sid)[0];

            //将总数转化为达成率(百分制)
            Double avgPercent=or.getTotalNumberOfLearners()/memberCountMap.get(or.getPathId())*100;
            java.text.DecimalFormat df =new java.text.DecimalFormat("#.000");
            String s = df.format(avgPercent);
            avgPercent = Double.parseDouble(s);

            avg_temps[indexKey] =avgPercent;
            pathIdMappingTotalNumsArrayMap.put(pid,avg_temps);
        }

        List<Object[]> results = RegularTransferMapToEchartsLineDataSet(stageIdMappingIndexMap,pathIdMappingTotalNumsArrayMap,pathIdToNameMap);

        return results;
    }

    private List<Object[]> RegularTransferMapToEchartsLineDataSet(
            Map<Integer,Object[]> headMap,
            Map<Integer,Double[]> rowMap,
            Map<Integer,String>    pathIdToNameMap){

        List<Object[]> results=new ArrayList<>();

        //head转换
        Object[] heads=new Object[headMap.size()+1];
        headMap.forEach((key,value)->{
            heads[(Integer) value[0]+1]= value[1];
        });
        //无所谓,统一格式好看
        heads[0]="product";

        results.add(heads);

        rowMap.forEach((key, value)->{
            Object[] row_temp = new Object[value.length+1];
            for (int i = 0; i < value.length; i++) {
                row_temp[i+1]=value[i];


                if(value[i]==null||value[i].equals(0.0)){
                    row_temp[i+1]='-';
                }
            }
            String pathName = PythonUtils.ExtractLastOrgNameFromArrayStr(pathIdToNameMap.get(key));
            row_temp[0] = pathName;
            results.add(row_temp);
        });

        return results;
    }

    private Map<Integer,OrgRanking> findOrgRanking(Integer uid,Integer startStageIndex,Integer endStageNum){

        //获取每个组织下的平均完成人数
        List<Member> mList = memberService.findAvgOfGroupByPathCount(uid,startStageIndex,endStageNum);
        //获取member以及对应的总数
        Map<Integer,Double> memberCountMap=getTotalNumOfMemberGroupByPathMap(uid);

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
        Map<Integer,OrgRanking> orgRankingPathIdMap = new LinkedHashMap<>();
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

    private Map<Integer, OrgRanking> setRankingTrend(Map<Integer,OrgRanking> currentStageRankMap, Map<Integer,OrgRanking> beforeStageRankMap ){

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

    /**
     * PathId:TotalNums
     * @param uid
     * @return
     */
    private Map<Integer,Double> getTotalNumOfMemberGroupByPathMap(Integer uid){
        //获取member以及对应的总数
        List<Member> memberList = memberService.findCountOfGroupByPathId(uid);
        Map<Integer,Double> memberCountMap=new HashMap<Integer,Double>();
        for (Member m:memberList) {
            memberCountMap.put(m.getPathId(),m.getCount());
        }
        return memberCountMap;
    }
}
