package com.example.nacosexample.controller;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


@RestController
@RequestMapping("/")
public class HelloController {

    @NacosValue(value = "${example.useLocalCache:false}", autoRefreshed = true)
    private boolean useLocalCache;

    @NacosValue(value = "${example.name:default}", autoRefreshed = true)
    private String name;

    @RequestMapping(value = "/get", method = GET)
    @ResponseBody
    public boolean get() {
        return useLocalCache;
    }

    @RequestMapping(value = "/name", method = GET)
    @ResponseBody
    public String getName() {
        return name;
    }

    @RequestMapping("hello")
    public String sayHello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hello " + name + "!";
    }

}
