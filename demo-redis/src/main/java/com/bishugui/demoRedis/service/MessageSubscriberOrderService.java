package com.bishugui.demoRedis.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

/**
 * @author bi shugui
 * @description redis消息接收服务,order服务,实现MessageListener
 * @date 2022/12/1 22:10
 */
@Slf4j
@Service
public class MessageSubscriberOrderService implements MessageListener {
    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("redis subscriber order service:{}",message);
    }
}
