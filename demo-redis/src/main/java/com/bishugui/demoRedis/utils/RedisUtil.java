package com.bishugui.demoRedis.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author Shugui Bi
 * @Description redis工具
 * @date 2022/04/08 23:25:08
 */
@Slf4j
@Component
public class RedisUtil {

    /**
     * redisTemplate
     */
    private static RedisTemplate<String,Object> redisTemplate;

    /**
     * stringRedisTemplate String专用
     */
    private static StringRedisTemplate stringRedisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        RedisUtil.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        RedisUtil.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 缓存过期默认时间，单位：分钟
     */
    private static final int EXPIRE_DEFAULT_MINUTES = 60*24;

    public static RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    public static StringRedisTemplate getStringRedisTemplate() {
        return stringRedisTemplate;
    }

    public static Boolean delete(String key){
        return redisTemplate.delete(key);
    }

    /**********************String 类型**************************/

    /**
     * 设置字符串
     * @param key 键名
     * @param strValue 值
     * @param timeout 过期时间
     * @param timeUnit 时间单位
     */
    public static void setForString(String key,String strValue,long timeout,TimeUnit timeUnit){
        stringRedisTemplate.opsForValue().set(key,strValue,timeout,timeUnit);
    }

    public static String getForString(String key){
        return stringRedisTemplate.opsForValue().get(key);
    }


    /**********************Object 类型**************************/
    /**
     * 设置对象值
     * @param key 键名
     * @param objectValue 值
     * @param timeout 过期时间
     * @param timeUnit 时间单位
     */
    public static void setForObject(String key,Object objectValue,long timeout,TimeUnit timeUnit){
        redisTemplate.opsForValue().set(key,objectValue,timeout,timeUnit);
    }

    /**
     * 设置值，默认超时EXPIRE_DEFAULT_MINUTES
     * @param key 键名
     * @param objectValue 值
     */
    public static void setForObject(String key,Object objectValue){
        redisTemplate.opsForValue().set(key,objectValue,EXPIRE_DEFAULT_MINUTES,TimeUnit.MINUTES);
    }

    /**
     * 获取对象值
     * @param key 键名
     * @return 返回对象泛型
     * @param <T> 泛型
     */
    public static<T> T getForObject(String key){
        return (T)redisTemplate.opsForValue().get(key);
    }
}
