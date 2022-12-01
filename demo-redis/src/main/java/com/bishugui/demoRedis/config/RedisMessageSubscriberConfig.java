package com.bishugui.demoRedis.config;

import com.bishugui.demoRedis.service.MessageSubscriberOrderService;
import com.bishugui.demoRedis.service.MessageSubscriberUserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * @description redis pub/sub中的消息接收者服务配置
 * @author bi shugui
 * @date 2022/12/1 21:39
 */
@Configuration
public class RedisMessageSubscriberConfig {
    @Value("${redis.msg-topic.user}")
    private String redisMsgTopicUser;

    @Value("${redis.msg-topic.order}")
    private String redisMsgTopicOrder;

    @Resource
    private MessageSubscriberUserService messageSubscriberUserService;

    @Resource
    private MessageSubscriberOrderService messageSubscriberOrderService;

    /**
     * 消息监听容器，可以添加多个消息监听适配器
     * @param redisConnectionFactory RedisConnectionFactory
     * @return RedisMessageListenerContainer
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory){
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
        //添加适配器,可添加多个
        redisMessageListenerContainer.addMessageListener(userServiceAdapter(),new ChannelTopic(redisMsgTopicUser));
        redisMessageListenerContainer.addMessageListener(orderServiceAdapter(),new ChannelTopic(redisMsgTopicOrder));
        //new PatternTopic("/topicName/*");可匹配多个渠道，如发布时的渠道为：/topicName/first、/topicName/second
        return redisMessageListenerContainer;
    }

    /**
     * 消息监听 适配器
     * @return MessageListenerAdapter
     */
    public MessageListenerAdapter userServiceAdapter(){
        return new MessageListenerAdapter(messageSubscriberUserService);
    }

    public MessageListenerAdapter orderServiceAdapter(){
        return new MessageListenerAdapter(messageSubscriberOrderService);
    }
}
