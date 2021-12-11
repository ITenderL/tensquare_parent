package com.tensquare;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import util.IdWorker;

/**
 * @Author: HeWei·Yuan
 * @CreateTime: 2021-03-25 17:56
 * @Description:
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@MapperScan("com.tensquare.notice.dao")
public class NoticeApplication {
    public static void main(String[] args) {
        SpringApplication.run(NoticeApplication.class, args);
    }

    @Bean
    public IdWorker idWorker() {
        return new IdWorker(1,1);
    }
}
