package com.example.serviceprovider.redis;

import com.example.common.util.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import java.net.UnknownHostException;
import java.util.Collections;

/**
 * 类说明：Reids集群工具类
 * <p>
 * 详细描述：Reids集群工具类
 *
 * @author Jiang
 * @since 2019年08月05日
 */

@Component
public class RedisClusterHelper {

    @Autowired
    private JedisCluster jedisCluster;

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "EX";  // EX|PX, expire time units: EX = seconds; PX = milliseconds

    /**
     * 获取分布式锁
     * @param lockName 锁key
     * @param ttl 过期时间，单位为s
     * @return result
     * @throws UnknownHostException exception
     */
    public boolean tryLock(String lockName, long ttl) throws UnknownHostException {
        String localIp = IpUtils.getLocalIp();
        // 当redis中lockName不存在时，设置lockName值为localIp，并设置超时时间为ttl秒，设置成功返回ok
        String result = jedisCluster.set(lockName, localIp, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, ttl);

        return LOCK_SUCCESS.equals(result);
    }

    /**
     * 释放分布式锁
     * @param lockName key
     * @return result
     */
    public boolean releaseLock(String lockName) throws UnknownHostException {
        String localIp = IpUtils.getLocalIp();
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

        Object result = jedisCluster.eval(script, Collections.singletonList(lockName), Collections.singletonList(localIp));
        return LOCK_SUCCESS.equals(result);
    }

}
