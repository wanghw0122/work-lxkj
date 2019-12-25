package com.rcplatformhk.us.datasource.config;


import com.alibaba.druid.pool.DruidDataSource;
import com.rcplatformhk.us.common.DatabaseType;
import com.rcplatformhk.us.datasource.DynamicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@MapperScan(basePackages="com.rcplatformhk.us.dao.mapper", sqlSessionFactoryRef="sessionFactory")
public class MybatisConfig {
    @Autowired
    Environment environment;

    @Value("${datasource.jdbc.driverClassName}")
    private String dbDriver;

    @Value("${datasource.jdbc.url_major}")
    private String dbUrl;

    @Value("${datasource.jdbc.username_major}")
    private String dbUsername;

    @Value("${datasource.jdbc.password_major}")
    private String dbPassword;

    @Value("${datasource.jdbc.url_secondary}")
    private String dbUrl_secondary;

    @Value("${datasource.jdbc.username_secondary}")
    private String dbUsername_secondary;

    @Value("${datasource.jdbc.password_secondary}")
    private String dbPassword_secondary;


    /**
     * 创建主数据源
     * @throws Exception
     */
    @Bean(name="dataSourceMajor")
    public DataSource dataSourceLocal() throws Exception{
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(dbDriver);
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }

    /**
     * 创建次数据源
     */
    @Bean(name="dataSourceSecondary")
    public DataSource dataSourceStaging() throws Exception{
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(dbDriver);
        dataSource.setUrl(dbUrl_secondary);
        dataSource.setUsername(dbUsername_secondary);
        dataSource.setPassword(dbPassword_secondary);

        return dataSource;
    }

    /**
     * 1、创建动态数据源
     * @throws Exception
     * @Primary该注解表示在同一个接口有多个类可以注入的时候，默认选择哪个，而不是让@Autowired报错
     */
    @Bean(name="dynamicDataSource")
    @Primary
    public DynamicDataSource DataSource(@Qualifier("dataSourceMajor") DataSource dataSourceMajor,
                                        @Qualifier("dataSourceSecondary") DataSource dataSourceSecondary){
        Map<Object, Object> targetDataSource = new HashMap<>();
        targetDataSource.put(DatabaseType.major, dataSourceMajor);
        targetDataSource.put(DatabaseType.secondary, dataSourceSecondary);
        DynamicDataSource dataSource = new DynamicDataSource();
        dataSource.setTargetDataSources(targetDataSource);
        dataSource.setDefaultTargetDataSource(dataSourceMajor);

        return dataSource;
    }

    /**
     * 2、根据数据源创建SqlSessionFactory
     * @throws Exception
     */
    @Bean(name="sessionFactory")
    public SqlSessionFactory sessionFactory(@Qualifier("dynamicDataSource")DynamicDataSource dataSource) throws Exception{
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        return sessionFactoryBean.getObject();
    }
}
