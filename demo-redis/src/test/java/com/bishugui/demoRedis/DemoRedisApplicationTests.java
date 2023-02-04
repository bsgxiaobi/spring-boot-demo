package com.bishugui.demoRedis;

import com.bishugui.demoRedis.utils.RedisUtil;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class DemoRedisApplicationTests {
    static final String keyPrefix = "test_multi:";
    @Test
    void setMultiValue() {
        for(int i = 0;i<1000;i++){
            RedisUtil.setForObject(keyPrefix + i,i);
        }
        System.out.println("setMultiValue success");
    }

    /**
     * 测试MultiGet与管道的差异
     */
    void testMultiGetAndPipeline(int len){
        List<String> keyList = new ArrayList<>(len);
        for(int i = 0;i<len;i++){
            keyList.add(keyPrefix + i);
        }
        System.out.println("key数量:" + len);
        List<Integer> valueList;
        //循环跑5次，
        for(int k = 0;k<10;k++){
            long startTime = System.nanoTime();

            RedisUtil.multiGet(keyList);
            long multiGetTime = System.nanoTime();

            RedisUtil.multiGetUsePipeline(keyList);
            long pipelineTime = System.nanoTime();

            //keyList.forEach(RedisUtil::getForObject);
            //long forEachTime = System.nanoTime();

            System.out.printf("轮次:%d,multiGet用时:%d us,pipeline用时:%d us %n",
                    k,(multiGetTime-startTime)/1000,(pipelineTime-multiGetTime)/1000);
            //System.out.printf("轮次:%d,multiGet用时:%d us,pipeline用时:%d us,forEach用时:%d us %n",
            //        k,(multiGetTime-startTime)/1000,(pipelineTime-multiGetTime)/1000,(forEachTime-pipelineTime)/1000);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("\n");
    }

    @Test
    void testMultiGetAndPipeline(){
        testMultiGetAndPipeline(10);
        testMultiGetAndPipeline(20);
        testMultiGetAndPipeline(50);
        testMultiGetAndPipeline(100);
        testMultiGetAndPipeline(200);
        testMultiGetAndPipeline(500);
        testMultiGetAndPipeline(1000);
    }
}
