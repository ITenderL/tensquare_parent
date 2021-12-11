package com.tensquare.notice.config;

import com.tensquare.notice.netty.NettyServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: HeWei·Yuan
 * @CreateTime: 2021-06-11 09:26
 * @Description:
 */
@Configuration
public class NettyConfig {
    @Bean
    public NettyServer createNettyServer() {
        NettyServer nettyServer = new NettyServer();
        // 启动netty服务
        new Thread(){
            @Override
            public void run() {
                nettyServer.start(1234);
            }
        }.start();
        return nettyServer;
    }
}
