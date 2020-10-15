package com.example.demo.controller;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author ying.jiang
 * @define Todo
 * @date 2020-10-15-13:50:00
 */
@Controller
@RequestMapping("discovery")
public class DiscoveryController {

    @NacosInjected
    private NamingService namingService;

    private RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/hello")
    public String consumer() throws IllegalStateException, NacosException {
        // <1> 获得实例
        Instance instance = null;
        if (false) {
            List<Instance> instances = namingService.getAllInstances("nacos-provider");
            // 获得首个实例，进行调用
            instance = instances.stream().findFirst()
                    .orElseThrow(() -> new IllegalStateException("未找到对应的 Instance"));
        } else {
            instance = namingService.selectOneHealthyInstance("nacos-provider");
        }
        // <2> 执行请求
        return restTemplate.getForObject("http://" + instance.toInetAddr() + "/provider/hello",
                String.class);
    }
}