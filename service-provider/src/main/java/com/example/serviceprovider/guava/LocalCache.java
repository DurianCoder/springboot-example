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