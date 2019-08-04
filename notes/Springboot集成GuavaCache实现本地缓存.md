# Springboot集成本地缓存Guava
本章介绍一个比较好用的本地缓存Guava
## 0x01、添加依赖
```
<dependency>
    <groupId>com.google.guava</groupId>
    <artifactId>guava</artifactId>
    <version>20.0</version>
</dependency>
```

## 0x02、添加[GuavaLocalCache](https://github.com/DurianCoder/springboot-example/blob/master/service-provider/src/main/java/com/example/serviceprovider/guava/LocalCache.java)
提供了设置超时时间、最大个数、无数据时从mysql加载特性
```
package com.example.serviceprovider.guava;

import com.example.serviceprovider.mysql.domain.bigdata.User;
import com.example.serviceprovider.mysql.mapper.bigdata.UserMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 类说明：Guava本地缓存类
 * <p>
 * 详细描述：Guava本地缓存类
 *
 * @author Jiang
 * @since 2019年08月04日
 */

@Component
public class LocalCache {

    @Autowired
    private UserMapper userMapper;

    /**
     * 本地缓存
     */
    private LoadingCache<String, User> userCache = CacheBuilder.newBuilder()
            .expireAfterWrite(20, TimeUnit.MINUTES)
            .maximumSize(1000)
            .build(new CacheLoader<String, User>() {
                @Override
                public User load(String userId) throws Exception {
                    User user = userMapper.selectByPrimaryKey(Integer.valueOf(userId));
                    if (null == user) {
                        return new User();
                    }
                    return user;
                }
            });

    /**
     * 获取User
     * @param id id
     * @return User
     */
    public User getUser(int id) {
        User user = null;
        try {
            user = userCache.get(id + "");
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return user;
    }

}
```

## 0x03、使用guavaCache
```
    @Autowired
    LocalCache localCache;

    /**
     * 测试guawa本地缓存
     */
    @Test
    public void testGuava() {
        User user = localCache.getUser(1);
        System.out.println(user.getUserName());
    }
```