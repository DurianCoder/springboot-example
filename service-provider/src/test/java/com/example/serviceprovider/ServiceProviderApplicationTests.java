package com.example.serviceprovider;

import com.example.serviceprovider.mysql.domain.UserLog;
import com.example.serviceprovider.mysql.domain.UserLogExample;
import com.example.serviceprovider.mysql.mapper.UserLogMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceProviderApplicationTests {

    @Autowired
    public UserLogMapper userLogMapper;

    @Test
    public void contextLoads() {
        List<UserLog> userLogs = userLogMapper.selectByExample(new UserLogExample());
        System.out.println(userLogs.get(0).getLogDetail());

    }

}
