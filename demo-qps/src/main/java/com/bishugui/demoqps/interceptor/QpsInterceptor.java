package com.bishugui.demoqps.interceptor;

import com.bishugui.demoqps.model.QpsBO;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author bi shugui
 * @description qps拦截器
 * @date 2023/2/7 21:18
 */
@Slf4j
@Component
public class QpsInterceptor implements HandlerInterceptor {

    /**
     * 统计范围单位：秒
     * 如统计每1秒 每5秒
     */
    private static final Integer RANGE_SECOND = 1;

    private static final String KEY_TOTAL_COUNT = "ALL";

    /**
     * api 请求数量map
     */
    private Map<String, QpsBO> apiRequestCountMap = new ConcurrentHashMap<>(128);

    /**
     * 请求总数
     */
    private Counter requestTotalCounter;

    @Resource
    private MeterRegistry meterRegistry;

    /**
     * 阻塞优先队列
     * add：添加元素,不会阻塞，添加成功时返回true，不响应中断，当队列已满导致添加失败时抛出IllegalStateException。
     * offer：添加元素.不会阻塞，添加成功时返回true，因队列已满导致添加失败时返回false，不响应中断。
     * put：添加元素,会阻塞会响应中断
     *
     * take：获取队头并移除，会响应中断，会一直阻塞直到取得元素或当前线程中断。
     * poll：获取队头并移除，会响应中断，会阻塞，阻塞时间参照方法里参数timeout.timeUnit，当阻塞时间到了还没取得元素会返回null
     *
     * peek: 获取队头不移除
     *  private static PriorityBlockingQueue<QpsBO> QPS_QUEUE = new PriorityBlockingQueue<>();
     */

    @PostConstruct
    public void init(){
        //总数、总qps
        requestTotalCounter = Counter.builder("api_request_total_count")
                .baseUnit("次").tag("api_path",KEY_TOTAL_COUNT).description("请求总数").register(meterRegistry);

        AtomicLong totalGauge = new AtomicLong(0L);
        Gauge.builder("api_rate", totalGauge,AtomicLong::get).tag("api_rate_path", KEY_TOTAL_COUNT).description("总QPS").baseUnit("次").register(meterRegistry);
        QpsBO qpsBO = QpsBO.builder()
                .qps(totalGauge)
                .lastCount(0L)
                .lastTime(System.currentTimeMillis())
                .apiPath(KEY_TOTAL_COUNT)
                .count(new AtomicLong(0)).build();
        apiRequestCountMap.put(KEY_TOTAL_COUNT,qpsBO);

        qpsQueueTask();
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String path = request.getServletPath();
        //增加接口调用总次数
        apiRequestCountMap.get(KEY_TOTAL_COUNT).getCount().incrementAndGet();
        try {
            //此处采用try-catch空指针的方式,最多catch 总接口个数 次并只会在第一次请求时调用，而containsKey(path)次数为总请求次数，减少调用
            apiRequestCountMap.get(path).getCount().incrementAndGet();
        }catch (NullPointerException e){
            log.warn("apiRequestCountMap,key:{}空指针,调用创建指标",path);
            //增加指定链接qps的指标器
            AtomicLong atomicQps = new AtomicLong(0L);
            Gauge.builder("api_rate", atomicQps,AtomicLong::get).tag("api_rate_path", path).description("接口" + path).baseUnit("次").register(meterRegistry);
            QpsBO qpsBO = QpsBO.builder()
                    .qps(atomicQps)
                    .lastCount(0L)
                    .lastTime(System.currentTimeMillis())
                    .apiPath(path)
                    .count(new AtomicLong(1)).build();
            apiRequestCountMap.put(path,qpsBO);
        }
    }

    public void qpsQueueTask(){
        //从队列中获取值
        new Thread(()->{
            log.info("qps计算任务");

            while (true){
                try {
                    Thread.sleep(RANGE_SECOND * 1000);
                    AtomicLong changeCount = new AtomicLong(0);
                    long currentTime = System.currentTimeMillis();
                    //遍历map
                    apiRequestCountMap.values().forEach(item->{
                        //变化数据 = 总数 - 上一次总数
                        changeCount.set(item.getCount().get() - item.getLastCount());
                        item.getQps().set(changeCount.get()/RANGE_SECOND);
                        //log.info("api:{},qps:{} /{}s",item.getApiPath(),item.getMeterGauge().get(),RANGE/1000);
                        //当前总数 变为 下一次的总数
                        item.setLastCount(item.getCount().get());
                        item.setLastTime(currentTime);

                        //更新采集的总数
                        if(!KEY_TOTAL_COUNT.equals(item.getApiPath())){
                            requestTotalCounter.increment(changeCount.get());
                        }
                    });
                } catch (Exception e) {
                    log.error("循环异常",e);
                }
            }
        }).start();

    }
}
