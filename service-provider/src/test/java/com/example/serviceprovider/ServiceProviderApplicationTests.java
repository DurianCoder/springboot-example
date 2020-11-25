package com.example.serviceprovider;

import com.example.serviceprovider.guava.LocalCache;
import com.example.serviceprovider.mysql.domain.UserLog;
import com.example.serviceprovider.mysql.domain.UserLogExample;
import com.example.serviceprovider.mysql.domain.bigdata.User;
import com.example.serviceprovider.mysql.mapper.UserLogMapper;
import com.example.serviceprovider.mysql.mapper.bigdata.UserMapper;
import com.example.serviceprovider.redis.RedisClusterHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.JedisCluster;

import java.net.UnknownHostException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceProviderApplicationTests {

    @Autowired
    public UserLogMapper userLogMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    JedisCluster jedisCluster;

    @Autowired
    LocalCache localCache;

    @Autowired
    private Environment env;

    @Value("${durian.jdbc.username}")
    private String userName;

    @Autowired
    RedisClusterHelper redisClusterHelper;

    @Test
    public void testDistributeLock() throws UnknownHostException {
        redisClusterHelper.tryLock("age", 10);
        System.out.println(jedisCluster.get("age"));
        System.out.println("do something...");
        redisClusterHelper.releaseLock("age");
    }

    /**
     * yml参数获取
     */
    @Test
    public void testYml() {
        System.out.println(userName);
        System.out.println(env.getProperty("durian.jdbc.username"));
    }


    /**
     * 测试guawa本地缓存
     */
    @Test
    public void testGuava() {
        User user = localCache.getUser(1);
        System.out.println(user.getClass());
    }

    /**
     * 测试RedisCluster
     */
    @Test
    public void testRedisCluster() {
        String name = jedisCluster.get("name");
        System.out.println(name);
    }


    /**
     * 测试bigdata数据源
     */
    @Test
    public void testMultiDs() {
        User user = userMapper.selectByPrimaryKey(1);
        System.out.println(user.getClass());
    }


    /**
     * 测试durian数据源
     */
    @Test
    public void contextLoads() {
        List<UserLog> userLogs = userLogMapper.selectByExample(new UserLogExample());
        System.out.println(userLogs.get(0).getLogDetail());
    }

}
