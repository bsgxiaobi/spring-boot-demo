package com.bishugui.demoqps.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicLong;


/**
 * @author bi shugui
 * @description qps 信息
 * @date 2023/2/7 21:46
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class QpsBO {
    /**
     * 请求总数
     */
    private AtomicLong count;

    /**
     * 上一次总数
     */
    private Long lastCount;

    /**
     * qps,prometheus的Gauge指标
     */
    private AtomicLong qps;

    /**
     * 上一次时间
     */
    private Long lastTime;

    /**
     * 路径
     */
    private String apiPath;
}
