package com.bishugui.demoRedis.controller;

import com.bishugui.demoRedis.model.BaseEntity;
import com.bishugui.demoRedis.utils.RedisUtil;
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
    @PostMapping("setForObj")
    public void setForObj(@RequestParam("key") String key){
        BaseEntity build = BaseEntity.builder().id(123456789L).createId("123").createDate(LocalDateTime.now()).build();
        RedisUtil.setForObject(key,build);
    }

    @GetMapping("getForObj")
    public BaseEntity getForObj(String key){
        BaseEntity baseEntity = RedisUtil.getForObject(key);
        return baseEntity;
    }
}
