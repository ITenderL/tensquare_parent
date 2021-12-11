package com.tensquare.notice.config;

import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: HeWei·Yuan
 * @CreateTime: 2021-03-21 19:55
 * @Description:
 */
@Configuration
public class MybatisPlusConfig {

    @Bean
    public PaginationInterceptor createPaginationInterceptor() {
        return new PaginationInterceptor();
    }
}
