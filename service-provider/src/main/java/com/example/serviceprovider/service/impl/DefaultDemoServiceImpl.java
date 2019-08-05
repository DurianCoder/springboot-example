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
