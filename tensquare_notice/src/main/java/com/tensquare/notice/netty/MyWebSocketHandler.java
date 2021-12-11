package com.tensquare.notice.netty;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tensquare.notice.config.ApplicationContextProvider;
import entity.Result;
import entity.StatusCode;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author: HeWei·Yuan
 * @CreateTime: 2021-06-11 09:27
 * @Description:
 */
public class MyWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static ObjectMapper MAPPER = new ObjectMapper();
    // 存放websocket链接Map，根据用户id存放
    public static ConcurrentMap<String, Channel> userChannelMap = new ConcurrentHashMap<String, Channel>();
    // 获取rabbitTemplate
    RabbitTemplate rabbitTemplate = ApplicationContextProvider.getApplicationContext().getBean(RabbitTemplate.class);
    // 从spring容器中获取消息监听器容器
    SimpleMessageListenerContainer sysNoticeContainer = (SimpleMessageListenerContainer) ApplicationContextProvider.getApplicationContext().getBean("sysNoticeContainer");
    SimpleMessageListenerContainer userNoticeContainer = (SimpleMessageListenerContainer) ApplicationContextProvider.getApplicationContext().getBean("userNoticeContainer");
    // 用户请求websocket服务端执行方法
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        // 约定用户第一次链接携带的数据{"userId","1"}
        // 获取请求数据并解析
        String text = msg.text();
        // 解析json数据，获取用户id
        String userId = MAPPER.readTree(text).get("userId").asText();
        // 第一次请求，建立websocket链接
        Channel channel = userChannelMap.get(userId);
        if (channel == null) {
            // 获取websocket链接
            channel = ctx.channel();
            // 把链接加到容器中
            userChannelMap.put(userId, channel);
        }
        // 获取rabbitMQ消息内容
        String queueName = "article_subscribe_" + userId;
        RabbitAdmin rabbitAdmin = new RabbitAdmin(rabbitTemplate.getConnectionFactory());
        Properties queueProperties = rabbitAdmin.getQueueProperties(queueName);
        // 获取消息数据
        int noticeCount = 0;
        if (queueProperties != null){
            noticeCount = (int) queueProperties.get("QUEUE_MESSAGE_COUNT");
        }
        /*************************************以上订阅类消息，以下点赞类消息**********************************/
        // 获取rabbitMQ消息内容
        String userQueueName = "article_thumbup_" + userId;
        Properties userQueueProperties = rabbitAdmin.getQueueProperties(userQueueName);
        // 获取消息数据
        int userNoticeCount = 0;
        if (userQueueProperties != null){
            userNoticeCount = (int) queueProperties.get("QUEUE_MESSAGE_COUNT");
        }
        // 封装返回数据
        Map countMap = new HashMap();
        countMap.put("sysNoticeCount", noticeCount);
        countMap.put("userNoticeCount", userNoticeCount);
        Result result = new Result(true, StatusCode.OK, "查询成功", countMap);
        // 把数据发送给用户
        channel.writeAndFlush(new TextWebSocketFrame(MAPPER.writeValueAsString(result)));
        // 把消息冲队列中清空
        if (noticeCount > 0) {
            rabbitAdmin.purgeQueue(queueName, true);
        }
        if (userNoticeCount > 0) {
            rabbitAdmin.purgeQueue(userQueueName, true);
        }
        // 为用户的消息通知队列创建监听器，便于用户在线的时候
        // 一旦有消息可以主动推送给用户，不需要用户请求服务获取数据
        sysNoticeContainer.addQueueNames(queueName);
        userNoticeContainer.addQueueNames(userQueueName);
    }
}
