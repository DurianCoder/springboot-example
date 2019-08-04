package com.example.serviceprovider.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;

/**
 * 类说明：Redis集群配置类
 * <p>
 * 详细描述：Redis集群配置类
 *
 * @author Jiang
 * @since 2019年08月04日
 */

@Configuration
public class JedisClusterConfig {
    @Autowired
    private Environment env;

    @Bean
    public JedisCluster jedisCluster() {
        String[] serverArray = env.getProperty("redis.cluster.servers").split(",");
        HashSet<HostAndPort> nodes = new HashSet<>();
        for (String ipPort : serverArray) {
            String[] ipPortPair = ipPort.split(":");
            nodes.add(new HostAndPort(ipPortPair[0].trim(), Integer.valueOf(ipPortPair[1].trim())));
        }
        return new JedisCluster(nodes, Integer.valueOf(env.getProperty("redis.cluster.commandTimeout")));
    }

}