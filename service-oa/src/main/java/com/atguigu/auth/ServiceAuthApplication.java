package com.atguigu.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author: 程志琨
 * @Description: 启动类配置
 * @Date: 2024/4/15 16:20
 * @Version: 1.0
 */


@SpringBootApplication
@ComponentScan("com.atguigu")
public class ServiceAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceAuthApplication.class, args);
    }
}
