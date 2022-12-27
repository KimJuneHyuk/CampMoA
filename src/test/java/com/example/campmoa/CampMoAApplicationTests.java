package com.example.campmoa;

import com.example.campmoa.mappers.IBbsMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
class CampMoAApplicationTests {
    @Autowired
    private IBbsMapper bbsMapper;
    @Test
    void contextLoads() {
        Arrays.stream(bbsMapper.selectArticles()).forEach(x -> System.out.println(x.getName()));
        System.out.println(bbsMapper.selectArticleByIndex(1).getTitle());
    }

}
