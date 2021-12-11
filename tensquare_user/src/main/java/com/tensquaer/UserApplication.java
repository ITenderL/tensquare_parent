package com.tensquaer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @Author: HeWei·Yuan
 * @CreateTime: 2021-03-24 11:22
 * @Description:
 */
@SpringBootApplication
@MapperScan("com.tensquaer.user.dao")
@EnableEurekaClient
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
