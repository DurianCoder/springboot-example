package com.example.serviceconsumer;

import com.alibaba.dubbo.config.annotation.Reference;
import com.example.api.service.IDemoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceConsumerApplicationTests {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Reference(version = "1.0.0")
    private IDemoService demoService;

    @Test
    public void contextLoads() {
        System.out.println("==============================");
        demoService.sayHello("Jiang");
    }

}
