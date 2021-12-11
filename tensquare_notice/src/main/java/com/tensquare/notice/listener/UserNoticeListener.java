package com.tensquare.notice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.tensquare.notice.netty.MyWebSocketHandler;
import entity.Result;
import entity.StatusCode;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: HeWei·Yuan
 * @CreateTime: 2021-06-11 09:26
 * @Description:
 */
public class UserNoticeListener implements ChannelAwareMessageListener {
    private static ObjectMapper MAPPER = new ObjectMapper();
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        // 获取用户id
        String queueName = message.getMessageProperties().getConsumerQueue();
        String userId = queueName.substring(queueName.lastIndexOf("_" ) + 1);
        io.netty.channel.Channel wsChannel = MyWebSocketHandler.userChannelMap.get(userId);
        // 判断用户是否在线
        if (wsChannel != null) {
            // 如果链接不为空，用户在线
            // 封装返回数据
            Map countMap = new HashMap();
            countMap.put("userNoticeCount", 1);
            Result result = new Result(true, StatusCode.OK, "查询成功", countMap);
            wsChannel.writeAndFlush(new TextWebSocketFrame(MAPPER.writeValueAsString(countMap)));
        }

    }
}
