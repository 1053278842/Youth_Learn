package com.ll.youthlearn.service;

import com.ll.youthlearn.entity.Member;
import com.ll.youthlearn.entity.MemberEachStage;
import com.ll.youthlearn.factory.IPythonSpider;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/3/8 15:48
 * @Description :
 */
@Slf4j
@WebAppConfiguration
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
class IStageServiceTest {

    @Autowired
    private IStageService stageService;

    @Autowired
    private IMemberEachStageService memberEachStageService;

    @Autowired
    private IMemberService memberService;

    @Resource(name = "pythonSpider")
    private IPythonSpider pythonSpider;


    @Test
    void findNewestStage() throws ParseException {

//        pythonSpider.saveAllStage();
        List<MemberEachStage> mesList = memberEachStageService.findCountOfGroupByPath(1,5);
        // 存放空间
        Map<Integer,List<MemberEachStage>> map=new HashMap<Integer,List<MemberEachStage>>();
        //根据stageId分组List
        for (MemberEachStage mes:mesList) {
            // 临时存储的当前stageId
            Integer stageIdTemp=mes.getStage_id();
            List<MemberEachStage> mesListTemp;

            //当stageId作为主键主线重复时Ps:必定重复
            if(!map.containsKey(stageIdTemp)){
                //创建一个具有相同StageId的list
                mesListTemp = new ArrayList<MemberEachStage>();
            }else{
                //添加重复主键对应的值
                mesListTemp = map.get(stageIdTemp);
            }
            mesListTemp.add(mes);
            map.put(stageIdTemp,mesListTemp);
        }

        //获取member以及对应的总数
        List<Member> memberList = memberService.findCountOfGroupByPathId(1);
        Map<Integer,Integer> memberCountMap=new HashMap<Integer,Integer>();
        for (Member m:memberList) {
            memberCountMap.put(m.getPathId(),m.getCountId());
        }

        for(HashMap.Entry<Integer,List<MemberEachStage>> entry : map.entrySet()){
            Integer sid=entry.getKey();
            List<MemberEachStage> mesListOfContainCount=entry.getValue();
            log.info(sid+"下:");
            for (MemberEachStage mes: mesListOfContainCount) {
                log.info("    "+mes.getMember().getCountId()+"|"+memberCountMap.get(mes.getMember().getPathId()));
            }
        }
    }
}