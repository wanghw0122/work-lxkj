package com.rcplatformhk.us;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@EnableCaching
//@MapperScan("com.rcplatformhk.us.dao.mapper")
@Slf4j
@SpringBootApplication(scanBasePackages = {"com.rcplatformhk"},exclude = {
        DataSourceAutoConfiguration.class,
        MybatisAutoConfiguration.class
})
public class UserpoolServerApplication{
    public static void main(String[] args) {
        SpringApplication.run(UserpoolServerApplication.class, args);
    }
}