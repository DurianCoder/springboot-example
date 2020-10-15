package com.example.nacosexample;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@NacosPropertySource(dataId = "nacos-example.properties", autoRefreshed = true)
public class NacosExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosExampleApplication.class, args);
    }

}
