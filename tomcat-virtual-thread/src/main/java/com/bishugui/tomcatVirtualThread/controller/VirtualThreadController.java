package com.bishugui.tomcatVirtualThread.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Executors;

/**
 * @author bi shugui
 * @description 虚拟线程测试Controller
 * @date 2023/9/23 20:42
 */
@RestController
@RequestMapping("/virtualThread")
public class VirtualThreadController {
    @GetMapping("/test")
    public String test() {
        try {
            Thread.sleep(600);
            return "hello virtual thread";
        } catch (InterruptedException e) {
            return "error";
        }
    }

    @GetMapping("/tomcatUseVirtualThreadPerTaskExecutor")
    public String tomcatUseVirtualThreadPerTaskExecutor() {
        try {
            Thread.sleep(300);
            return "tomcatUseVirtualThreadPerTaskExecutor，" + Thread.currentThread();
        } catch (InterruptedException e) {
            return "error";
        }
    }


    @GetMapping("/test1")
    public String test1() {
        Executors.newVirtualThreadPerTaskExecutor().execute(()-> System.out.println("virtual thread ," + Thread.currentThread()));
        //Thread.ofVirtual().start(()-> System.out.println("virtual thread start"));
        return "hello virtual thread";
    }
}
