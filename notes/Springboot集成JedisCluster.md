# Springboot集成缓存数据库Redis
几乎每个项目都会用到缓存数据库，本章将介绍使用springboot如何整合Redis集群，Redis的集群搭建以及系统调优等不在本章做介绍。
## 0x01、添加Maven依赖
```
<dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
</dependency>
```

## 0X02、添加redis配置，[application.properties](https://github.com/DurianCoder/springboot-example/blob/master/service-provider/src/main/resources/application.properties)
```
# redis config
redis.cluster.servers = 192.168.145.10:7000,192.168.145.10:7001,192.168.145.10:7002,192.168.145.11:7003,192.168.145.11:7004,192.168.145.11:7005
redis.cluster.commandTimeout = 5000
```

## 0X03、添加[JedisClusterConfig类](https://github.com/DurianCoder/springboot-example/blob/master/service-provider/src/main/java/com/example/serviceprovider/redis/JedisClusterConfig.java)
```
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
```

## 0x04、redis集群使用
```
    @Autowired
    JedisCluster jedisCluster;

    @Test
    public void testRedisCluster() {
        String name = jedisCluster.get("name");
        System.out.println(name);
    }
```

## 0X05、JedisCluster关键源码解析
在执行get操作是为什么不需要手动释放资源？

    下面是get()关键源码:重试之后在finally里做了资源释放this.releaseConnection(connection);
```
public String get(final String key) {
    return (String)(new JedisClusterCommand<String>(this.connectionHandler, this.maxAttempts) {
        public String execute(Jedis connection) {
            return connection.get(key);
        }
    }).run(key);
}

public T run(String key) {
    if (key == null) {
        throw new JedisClusterException("No way to dispatch this command to Redis Cluster.");
    } else {
        return this.runWithRetries(JedisClusterCRC16.getSlot(key), this.maxAttempts, false, false);
    }
}


private T runWithRetries(int slot, int attempts, boolean tryRandomNode, boolean asking) {
    if (attempts <= 0) {
        throw new JedisClusterMaxRedirectionsException("Too many Cluster redirections?");
    } else {
        Jedis connection = null;

        Object var7;
        try {
            if (asking) {
                connection = (Jedis)this.askConnection.get();
                connection.asking();
                asking = false;
            } else if (tryRandomNode) {
                connection = this.connectionHandler.getConnection();
            } else {
                connection = this.connectionHandler.getConnectionFromSlot(slot);
            }

            Object var6 = this.execute(connection);
            return var6;
        } catch (JedisNoReachableClusterNodeException var13) {
            throw var13;
        } catch (JedisConnectionException var14) {
            this.releaseConnection(connection);
            connection = null;
            if (attempts <= 1) {
                this.connectionHandler.renewSlotCache();
            }

            var7 = this.runWithRetries(slot, attempts - 1, tryRandomNode, asking);
        } catch (JedisRedirectionException var15) {
            if (var15 instanceof JedisMovedDataException) {
                this.connectionHandler.renewSlotCache(connection);
            }

            this.releaseConnection(connection);
            connection = null;
            if (var15 instanceof JedisAskDataException) {
                asking = true;
                this.askConnection.set(this.connectionHandler.getConnectionFromNode(var15.getTargetNode()));
            } else if (!(var15 instanceof JedisMovedDataException)) {
                throw new JedisClusterException(var15);
            }

            var7 = this.runWithRetries(slot, attempts - 1, false, asking);
            return var7;
        } finally {
            this.releaseConnection(connection);
        }

        return var7;
    }
}
```