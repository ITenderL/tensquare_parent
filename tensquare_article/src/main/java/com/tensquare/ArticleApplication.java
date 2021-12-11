package com.tensquare;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import util.IdWorker;

/**
 * @Author: HeWei·Yuan
 * @CreateTime: 2021-06-04 10:52
 * @Description:
 */
@SpringBootApplication
@MapperScan("com.tensquare.article.dao")
@EnableFeignClients
public class ArticleApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArticleApplication.class, args);
    }

    @Bean
    public IdWorker creatIdWork() {
        return new IdWorker(1,1);
    }
}
