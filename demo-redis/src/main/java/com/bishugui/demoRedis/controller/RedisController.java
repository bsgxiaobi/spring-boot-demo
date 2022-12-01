package com.bishugui.demoRedis.controller;

import com.bishugui.demoRedis.model.BaseEntity;
import com.bishugui.demoRedis.model.SimpleMessage;
import com.bishugui.demoRedis.service.RedisPublishService;
import com.bishugui.demoRedis.utils.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * @author bi shugui
 * @description redis controller
 * @date 2022/11/28 23:31
 */
@RestController
@RequestMapping("/redis")
public class RedisController {

    @Value("${redis.msg-topic.user}")
    private String redisMsgTopicUser;

    @Value("${redis.msg-topic.order}")
    private String redisMsgTopicOrder;

    @Resource
    RedisPublishService redisPublishService;

    @PostMapping("/setForObj")
    public void setForObj(@RequestParam("key") String key){
        BaseEntity build = BaseEntity.builder().id(123456789L).createId("123").createDate(LocalDateTime.now()).build();
        RedisUtil.setForObject(key,build);
    }

    @GetMapping("/getForObj")
    public BaseEntity getForObj(String key){
        BaseEntity baseEntity = RedisUtil.getForObject(key);
        return baseEntity;
    }

    /**
     * 测试redis的pub/sub,user服务
     * @param msgContent 消息内容
     */
    @PostMapping("/redisMsgTopicUser")
    public void redisMsgTopicTest(@RequestParam("msgContent") String msgContent){
        SimpleMessage simpleMessage = SimpleMessage.builder()
                .content(msgContent).publisher("bishugui").createDate(LocalDateTime.now()).build();
        redisPublishService.publish(redisMsgTopicUser,simpleMessage);
    }

    /**
     * 测试redis的pub/sub,user服务
     * @param msgContent 消息内容
     */
    @PostMapping("/redisMsgTopicOrder")
    public void redisMsgTopicOrder(@RequestParam("msgContent") String msgContent){
        SimpleMessage simpleMessage = SimpleMessage.builder()
                .content(msgContent).publisher("bishugui").createDate(LocalDateTime.now()).build();
        redisPublishService.publish(redisMsgTopicOrder,simpleMessage);
    }
}
