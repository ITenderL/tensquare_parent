package com.tensquare.notice.config;

import com.tensquare.notice.listener.SysNoticeListener;
import com.tensquare.notice.listener.UserNoticeListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: HeWei·Yuan
 * @CreateTime: 2021-06-11 09:28
 * @Description:
 */
@Configuration
public class RabbitConfig {
    @Bean("sysNoticeContainer")
    public SimpleMessageListenerContainer createSimpleMessageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        // 使用channel监听
        container.setExposeListenerChannel(true);
        // 设置自己的监听器
        container.setMessageListener(new SysNoticeListener());
        return container;
    }

    @Bean("userNoticeContainer")
    public SimpleMessageListenerContainer createUserNoticeContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        // 使用channel监听
        container.setExposeListenerChannel(true);
        // 设置自己的监听器
        container.setMessageListener(new UserNoticeListener());
        return container;
    }
}
