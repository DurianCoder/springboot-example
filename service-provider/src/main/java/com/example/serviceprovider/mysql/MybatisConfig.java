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
