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

    }

}
