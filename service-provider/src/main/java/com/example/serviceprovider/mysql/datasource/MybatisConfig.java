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
