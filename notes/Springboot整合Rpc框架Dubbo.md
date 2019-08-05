# Springboot整合Rpc框架Dubbo
在微服务模块中，各微服务之间的通信可以通过http交互，一般内部各服务之间可以通过rpc框架dubbo来提高通信性能，本章主要springboot如何整合dubbo

## 0x01、环境依赖
- 使用zookeeper做Dubbo的服务注册中心，需要安装zookeeper，zk安装不在本章介绍。
- 添加springboot-dubbo maven依赖, 参考[pom.xml](https://github.com/DurianCoder/springboot-example/blob/master/common/pom.xml)
```
<dependency>
    <groupId>com.alibaba.boot</groupId>
    <artifactId>dubbo-spring-boot-starter</artifactId>
    <version>0.2.0</version>
</dependency>
```
- 在api Module中定义需要注册的接口，[IDemoService].java(https://github.com/DurianCoder/springboot-example/blob/master/api/com/example/api/service/IDemoService.java)
```
package com.example.api.service;

/**
 * 类说明：api接口
 * <p>
 * 详细描述：api接口
 *
 * @author Jiang
 * @since 2019年08月05日
 */

public interface IDemoService {
    String sayHello(String name);
}

```

## 0x02、服务提供者
- 添加dubbo配置，[application.properties](https://github.com/DurianCoder/springboot-example/blob/master/service-provider/src/main/resources/application.properties)
```
# Dubbo 服务提供者配置
dubbo.application.name=dubbo-provider
dubbo.protocol.name=dubbo
dubbo.protocol.port=12345
dubbo.registry.address=zookeeper://192.168.1.54:2181

```
- 实现api接口,[DefaultDemoServiceImpl.java](https://github.com/DurianCoder/springboot-example/blob/master/service-provider/com/example/serviceprovider/service/impl/DefaultDemoService.java)
```
package com.example.serviceprovider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.example.api.service.IDemoService;

/**
 * 类说明：Demo impl
 * <p>
 * 详细描述：Demo impl
 *
 * @author Jiang
 * @since 2019年08月05日
 */

@Service
public class DefaultDemoServiceImpl implements IDemoService {
    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }
}

```

- springboot启动时，加载dubbo
添加@EnableDubbo注释
```
package com.example.serviceprovider;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubbo
@SpringBootApplication
public class ServiceProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceProviderApplication.class, args);
    }

}
```

## 0x03、服务消费者
- 在application.properties中添加dubbo配置
使用dubbo包里的@Reference注解从zk上获取服务调用
```
##Dubbo  服务消费者配置
dubbo.application.name=dubbo-cusomer
dubbo.registry.address=zookeeper://192.168.1.54:2181
```

- 从dubbo上消费服务
```
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

```
- 启动springboot时，加载dubbo
使用@EnableDubbo注解启动dubbo
```
package com.example.serviceconsumer;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@EnableDubbo
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class ServiceConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceConsumerApplication.class, args);
    }

}

```

- 测试
浏览器访问:`http://localhost:8080/hello`


## 参考连接
- [SpringBoot2.0整合Dubbo最简Demo](https://blog.csdn.net/u013848401/article/details/86543879)
- [Dubbo官网](http://dubbo.apache.org/zh-cn/)
