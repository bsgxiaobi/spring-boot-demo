package com.bishugui.demoRedis.service;

import com.bishugui.demoRedis.model.SimpleMessage;
import com.bishugui.demoRedis.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author bi shugui
 * @description redis消息发布实现
 * @date 2022/12/1 21:25
 */
@Slf4j
@Service
public class RedisPublishServiceImpl implements RedisPublishService {

    @Override
    public void publish(String topic,SimpleMessage simpleMessage) {
        RedisUtil.getRedisTemplate().convertAndSend(topic,simpleMessage);
        log.info("redis publish done,topic:{}, message:{}",topic,simpleMessage);
    }
}
