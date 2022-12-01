package com.bishugui.demoRedis.service;

import com.bishugui.demoRedis.model.SimpleMessage;

/**
 * @author bi shugui
 * @description redis消息发布接口
 * @date 2022/12/1 21:23
 */
public interface RedisPublishService {
    /**
     * 消息发送
     * @param topic 主题
     * @param simpleMessage 消息类
     */
    void publish(String topic,SimpleMessage simpleMessage);
}
