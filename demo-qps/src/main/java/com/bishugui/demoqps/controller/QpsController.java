package com.bishugui.demoqps.controller;

import com.bishugui.demoqps.interceptor.QpsInterceptor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bi shugui
 * @description qps controller
 * @date 2023/2/7 21:14
 */
@RestController
@RequestMapping("/api/qps")
public class QpsController {

    @GetMapping("/test1")
    public void testQps1(){
    }

    @GetMapping("/test2")
    public void testQps2(){

    }

    @GetMapping("/test3")
    public void testQps3(){

    }

    @GetMapping("/test4")
    public void testQps4(){

    }
    @GetMapping("/test5")
    public void testQps5(){

    }
}
