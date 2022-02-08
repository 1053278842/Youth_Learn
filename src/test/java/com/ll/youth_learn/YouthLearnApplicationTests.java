package com.ll.youth_learn;

import com.ll.youth_learn.entity.TopOrg;
import com.ll.youth_learn.mapper.ITopOrgMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
class YouthLearnApplicationTests {

    @Autowired
    private ITopOrgMapper topOrgMapper;

    @Test
    void contextLoads() {
        List<TopOrg> topOrgList = topOrgMapper.selectList(null);
        log.info("测试：slf4j#################################");
        for (TopOrg topOrg:topOrgList) {
            System.out.println(topOrg.toString());
        }
    }

}
