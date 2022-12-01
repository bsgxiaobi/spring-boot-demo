package com.bishugui.demoRedis.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

/**
 * @author bi shugui
 * @description redis消息接收服务,test服务,实现MessageListener
 * @date 2022/12/1 21:37
 */
@Slf4j
@Component
public class MessageSubscriberUserService implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("redis subscriber user service:{}",message);
    }
}
