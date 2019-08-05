package com.example.serviceconsumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.example.api.service.IDemoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类说明：Dubbo消费者
 * <p>
 * 详细描述：Dubbo消费者
 *
 * @author Jiang
 * @since 2019年08月05日
 */

@RestController
public class DemoController {

    @Reference
    private IDemoService demoService;

    @GetMapping("hello")
    public String sayHello(@RequestParam(value = "name", defaultValue = "World") String name) {
        System.out.println("===================");
        return demoService.sayHello(name);
    }

}
