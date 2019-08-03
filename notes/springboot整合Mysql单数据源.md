# springboot整合Mysql单数据源.md
## 0x01、搭建数据库环境
- 安装Mysql
- 建库建表
- [使用Mybatis-Generator生成数据库访问层](https://github.com/DurianCoder/springboot-example/blob/master/notes/Mybatis-Generator生成数据访问层.md)

## 0x02、Springboot集成Mybatis
- 添加相关依赖，注意Druid版本, [pom.xml](https://github.com/DurianCoder/springboot-example/blob/master/common/pom.xml)
```
<!--数据库相关 start -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>

<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
    <version>1.1.10</version>
</dependency>

<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.36</version>
</dependency>

<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.2.8</version>
</dependency>

<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis-spring</artifactId>
    <version>1.2.2</version>
</dependency>

<!--数据库相关 end -->
```
- 添加mysql连接配置
[application.properties](https://github.com/DurianCoder/springboot-example/blob/master/service-provider/src/main/resources/application.properties)
```
# datasouce config
durian.jdbc.driverClassName = com.mysql.jdbc.Driver
durian.jdbc.url = jdbc:mysql://192.168.145.10:3306/durian?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT
durian.jdbc.username = app
durian.jdbc.password = 123456
```

- [编写MybatisConfig类](https://github.com/DurianCoder/springboot-example/blob/master/service-provider/com.example.serviceprovider.mysql.datasource.MybatisConfig.java)

[Druid连接池详细参数配置](https://www.cnblogs.com/MaxElephant/p/8108304.html)
```
package com.example.serviceprovider.mysql;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * Mybatis配置类，创建dataSource --> 创建sqlSessionFactory  --> 创建sqlSession,获取对应mapper
 */

@Configuration
// 扫描mapper接口所在包
@MapperScan(basePackages = "com.example.serviceprovider.mysql.mapper")
public class MybatisConfig {

    @Autowired
    private Environment env;

    @Bean
    @Primary
    public DataSource dataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(env.getProperty("durian.jdbc.url"));
        datasource.setUsername(env.getProperty("durian.jdbc.username"));
        datasource.setPassword(env.getProperty("durian.jdbc.password"));
        datasource.setDriverClassName(env.getProperty("durian.jdbc.driverClassName"));

        return datasource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource ds) throws Exception {
        SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
        fb.setDataSource(ds);
        // 指定domain和mapper路径
        fb.setTypeAliasesPackage("com.example.serviceprovider.mysql.domain");
        fb.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapping/*.xml"));
        return fb.getObject();
    }
}

```