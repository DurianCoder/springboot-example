# springboot整合Mysql多数据源
## 0x01、搭建数据库环境
  - 安装Mysql
  - 建库建表
  - [使用Mybatis-Generator生成数据库访问层](https://github.com/DurianCoder/springboot-example/blob/master/notes/Mybatis-Generator生成数据访问层.md)

## 0x02、Springboot集成多数据源
步骤总结：
```
1、DatabaseType列出数据源
2、DatabaseContextHolder是一个线程安全的容器，提供了设置和获取DatabaseType的方法
3、DynamicDataSource继承了AbstarctRoutingDataSource并重写了其中的detaermineCurrentLookupKey()方法
4、在MybatisConfig生成两个数据源，并设置默认数据源
5、将DynamicDataSource用@Primary注释注入到SqlsessionFactory的dataSource属性中
6、将dao和mapper用package来区分不同的数据源，在使用aop来实现数据源动态切换
```


- 添加多数据源配置,[application.properties](https://github.com/DurianCoder/springboot-example/blob/master/service-provider/src/main/resources/application.properties)
```
# bigdata datasource
bigdata.jdbc.driverClassName = com.mysql.jdbc.Driver
bigdata.jdbc.url = jdbc:mysql://192.168.145.10:3306/bigdata?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT
bigdata.jdbc.username = app
bigdata.jdbc.password = 123456
```
- [添加DatabaseType枚举类](https://github.com/DurianCoder/springboot-example/blob/master/service-provider/src/main/java/com/example/serviceprovider/mysql/datasource/DatabaseType.java)
```
package com.example.serviceprovider.mysql.datasource;

/**
 * 数据源类型
 */
public enum DatabaseType {
    durian,bigdata
}

```
- [添加DatabaseContextHolder类](https://github.com/DurianCoder/springboot-example/blob/master/service-provider/src/main/java/com/example/serviceprovider/mysql/datasource/DatabaseContextHolder.java)
```
package com.example.serviceprovider.mysql.datasource;

/**
 * 数据源Holder
 */
public class DatabaseContextHolder {
    private static final ThreadLocal<DatabaseType> contextHolder = new ThreadLocal<>();

    public static void setDataBaseType(DatabaseType type) {
        contextHolder.set(type);
    }

    public static DatabaseType getDatabaseType() {
        return contextHolder.get();
    }

    public static void reset() {
        contextHolder.set(DatabaseType.durian);
    }
}
```
- 添加[DynamicDataSource类](https://github.com/DurianCoder/springboot-example/blob/master/service-provider/src/main/java/com/example/serviceprovider/mysql/datasource/DynamicDataSource.java)继承AbstractRoutingDataSource
Tips:

    注意注解@Bean,@Primary,@Qualifier的使用
```
package com.example.serviceprovider.mysql.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源
 */
public class DynamicDataSource extends  {

    @Override
    protected Object determineCurrentLookupKey() {
        return DatabaseContextHolder.getDatabaseType();
    }
}

```
- [修改MybatisConfig类](https://github.com/DurianCoder/springboot-example/blob/master/service-provider/src/main/java/com/example/serviceprovider/mysql/datasource/MybatisConfig.java)
```
package com.example.serviceprovider.mysql.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.HashMap;

/**
 * Mybatis配置类，创建dataSource --> 创建sqlSessionFactory  --> 创建sqlSession,获取对应mapper
 */

@Configuration
@MapperScan(basePackages = "com.example.serviceprovider.mysql.mapper")  // 扫描mapper接口所在包
public class MybatisConfig {

    @Autowired
    private Environment env;

    @Bean
    public DataSource durianDataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(env.getProperty("durian.jdbc.url"));
        datasource.setUsername(env.getProperty("durian.jdbc.username"));
        datasource.setPassword(env.getProperty("durian.jdbc.password"));
        datasource.setDriverClassName(env.getProperty("durian.jdbc.driverClassName"));

        return datasource;
    }

    @Bean
    public DataSource bigdataDataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(env.getProperty("bigdata.jdbc.url"));
        datasource.setUsername(env.getProperty("bigdata.jdbc.username"));
        datasource.setPassword(env.getProperty("bigdata.jdbc.password"));
        datasource.setDriverClassName(env.getProperty("bigdata.jdbc.driverClassName"));

        return datasource;
    }


    /**
     * 主要数据源
     * @param durian @Qualifier
     * @param bigdata @Qualifier
     * @return DynamicDataSource
     */
    @Bean
    @Primary
    public DynamicDataSource dataSource(@Qualifier("durianDataSource") DataSource durian, @Qualifier("bigdataDataSource") DataSource bigdata) {
        HashMap<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DatabaseType.durian, durian);
        targetDataSources.put(DatabaseType.bigdata, bigdata);

        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.setDefaultTargetDataSource(durian);

        return dynamicDataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DynamicDataSource ds) throws Exception {
        SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
        fb.setDataSource(ds);
        // 指定domain和mapper路径
        fb.setTypeAliasesPackage("com.example.serviceprovider.mysql.domain");
        fb.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapping/*.xml"));
        return fb.getObject();
    }
}
```

- 添加aop相关依赖
```
<!--AOP依赖 start-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>

<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjrt</artifactId>
    <version>1.9.2</version>
</dependency>

<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjweaver</artifactId>
    <version>1.9.2</version>
</dependency>

<dependency>
    <groupId>cglib</groupId>
    <artifactId>cglib</artifactId>
    <version>2.1</version>
</dependency>

<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-aop</artifactId>
    <version>4.3.16.RELEASE</version>
</dependency>

<!--AOP依赖 end-->
```

- 添加aop切面，实现数据源自动切换,[DataSourceAspect类](https://github.com/DurianCoder/springboot-example/blob/master/service-provider/src/main/java/com/example/serviceprovider/mysql/datasource/DataSourceAspect.java)
Tips:主要一定要导入aspectj下的包
```
package com.example.serviceprovider.mysql.datasource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 类说明：数据源Aop
 * <p>
 * 详细描述：当使用bigdata包下的mapper时，自动切换数据源
 *
 * @author Jiang
 * @since 2019年08月03日
 */

@Aspect
@Component
public class DataSourceAspect {

    /**
     * 注意要引入aspectj包下面的类
     * @param joinPoint joinPoint
     * @return Object
     * @throws Throwable e
     */
    @Around(value = "execution(* com.example.serviceprovider.mysql.mapper.bigdata.*.*(..))")
    public Object setDataSourceKey(ProceedingJoinPoint joinPoint) throws Throwable {
        DatabaseContextHolder.setDataBaseType(DatabaseType.bigdata);
        Object object = joinPoint.proceed();
        DatabaseContextHolder.reset();

        return object;
    }
}
```

- 验证多数据源
```
package com.example.serviceprovider;

import com.example.serviceprovider.mysql.domain.UserLog;
import com.example.serviceprovider.mysql.domain.UserLogExample;
import com.example.serviceprovider.mysql.domain.bigdata.User;
import com.example.serviceprovider.mysql.mapper.UserLogMapper;
import com.example.serviceprovider.mysql.mapper.bigdata.UserMapper;
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

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testMultiDs() {
        User user = userMapper.selectByPrimaryKey(1);
        System.out.println(user.getUserName());
    }


    @Test
    public void contextLoads() {
        List<UserLog> userLogs = userLogMapper.selectByExample(new UserLogExample());
        System.out.println(userLogs.get(0).getLogDetail());

    }

}

```
