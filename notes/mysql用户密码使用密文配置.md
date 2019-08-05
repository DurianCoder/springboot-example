# Springboot整合Mysql使用Druid加密用户密码
## 0x01、使用druid工具生产密文
使用druid jar包工具生产密码和公钥：`java -cp druid-1.0.26.jar com.alibaba.druid.filter.config.ConfigTools 123456`
<img src="https://raw.githubusercontent.com/DurianCoder/springboot-example/master/notes/imgs/druid-public-key-1564975567.jpg" />

## 0X02、修改mysql连接属性配置文件
```
# durian datasource
durian:
  jdbc:
    driverClassName: com.mysql.jdbc.Driver
    url: jdbc:mysql://192.168.1.52:3306/durian?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT
    username: app
    password: AI7QKmenecZTovilwgPdds6x2qutcjBFzkmoBDkiiUwwili2s7YEjC+sOvI7BjQ7doQHRjzm03Fu0k6x9c+18g==
  config:
    decrypt: true
    druid:
      publickey: MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKFMEqQEFxSq4qmr2prehqVU88+s4qp7kpgn/Yi80rV6DdJCJsxXwXkwJg2dy0jrsndXZq125FcfWJIWrGezXtECAwEAAQ==


# bigdata datasource
bigdata:
  jdbc:
    driverClassName: com.mysql.jdbc.Driver
    url: jdbc:mysql://192.168.1.52:3306/bigdata?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT
    username: app
    password: AI7QKmenecZTovilwgPdds6x2qutcjBFzkmoBDkiiUwwili2s7YEjC+sOvI7BjQ7doQHRjzm03Fu0k6x9c+18g==
  config:
    decrypt: true
    druid:
      publickey: MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKFMEqQEFxSq4qmr2prehqVU88+s4qp7kpgn/Yi80rV6DdJCJsxXwXkwJg2dy0jrsndXZq125FcfWJIWrGezXtECAwEAAQ==

```

## 0X03、修改MybatisConfig类
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
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

/**
 * Mybatis配置类，创建dataSource --> 创建sqlSessionFactory  --> 创建sqlSession,获取对应mapper
 */

@Configuration
@MapperScan(basePackages = "com.example.serviceprovider.mysql.mapper")  // 扫描mapper接口所在包
public class MybatisConfig {

    @Autowired
    private Environment env;

    @Bean
    public DataSource durianDataSource() throws SQLException {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(env.getProperty("durian.jdbc.url"));
        datasource.setUsername(env.getProperty("durian.jdbc.username"));
        datasource.setPassword(env.getProperty("durian.jdbc.password"));
        datasource.setDriverClassName(env.getProperty("durian.jdbc.driverClassName"));

        // 指定druid public key
        datasource.setFilters("config");
        Properties properties = new Properties();
        properties.setProperty("config.decrypt", env.getProperty("durian.config.decrypt"));
        properties.setProperty("config.decrypt.key", env.getProperty("durian.config.druid.publickey"));

        datasource.setConnectProperties(properties);

        return datasource;
    }

    @Bean
    public DataSource bigdataDataSource() throws SQLException {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(env.getProperty("bigdata.jdbc.url"));
        datasource.setUsername(env.getProperty("bigdata.jdbc.username"));
        datasource.setPassword(env.getProperty("bigdata.jdbc.password"));
        datasource.setDriverClassName(env.getProperty("bigdata.jdbc.driverClassName"));

        // 指定druid public key
        datasource.setFilters("config");
        Properties properties = new Properties();
        properties.setProperty("config.decrypt", env.getProperty("bigdata.config.decrypt"));
        properties.setProperty("config.decrypt.key", env.getProperty("bigdata.config.druid.publickey"));

        datasource.setConnectProperties(properties);

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

## 0X04、测试
此时使用druid加密mysql用户密码已完成，可以使用单元测试进行测试。