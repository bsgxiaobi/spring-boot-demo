package com.bishugui.demoqps.interceptor;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.util.ObjectUtil;
import com.bishugui.demoqps.model.QpsBO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
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
     * 统计范围单位：毫秒
     * 如统计每1秒 每5秒
     */
    private static final Integer RANGE = 1;

    /**
     * qps计数
     */
    //private static TimedCache<String, AtomicLong> QPS_CACHE = CacheUtil.newTimedCache(RANGE * 5 * 1000);
    private static Map<String,AtomicLong> QPS_CACHE = new ConcurrentHashMap<>(1000);

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
     */
    private static PriorityBlockingQueue<QpsBO> QPS_QUEUE = new PriorityBlockingQueue<>(1200);

    /**
     * SimpleDateFormat
     */
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static {
        //从队列中获取值
        new Thread(()->{
            log.info("开始处理QPS队列");
            long currentTime = System.currentTimeMillis() / 1000;
            while (true){
                try {
                    //获取队头
                    QpsBO qpsBO = QPS_QUEUE.peek();
                    if(ObjectUtil.isNotNull(qpsBO) && qpsBO.getExpireSecond() < currentTime){
                        if(QPS_CACHE.containsKey(qpsBO.getKey())){
                            log.info("时间:{},path:{},qps:{}",
                                    SIMPLE_DATE_FORMAT.format(new Date(qpsBO.getExpireSecond() * 1000)),
                                    qpsBO.getApiPath(),
                                    QPS_CACHE.get(qpsBO.getKey()).get()
                            );
                        }
                        QPS_CACHE.remove(qpsBO.getKey());
                        //移除队头，因为已先queue.peek()，所以肯定有元素
                        QPS_QUEUE.take();
                        continue;
                    }
                    //若 无元素 或 未到期则
                    Thread.sleep(2000);
                    currentTime = System.currentTimeMillis() / 1000;
                } catch (Exception e) {
                    log.error("循环异常",e);
                }
            }
        }).start();

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //缓存key秒数
        long currTimeSecond = System.currentTimeMillis() / 1000;
        //范围时间开始，如 按分钟统计qps时，当前为05秒，但起始时间是当前分钟的00算。判断是否为每1秒统计一次，减少计算
        long rangStartTime = RANGE == 1?currTimeSecond:currTimeSecond - currTimeSecond % RANGE;

        String path = request.getServletPath();
        String key = rangStartTime + ":" + path;
        if(QPS_CACHE.containsKey(key)){
            QPS_CACHE.get(key).incrementAndGet();
        }else{
            //增加qps
            QPS_CACHE.put(key,new AtomicLong(1));
            //添加到队列中,expireSecond:到期时间，所以需要加上range
            QPS_QUEUE.add(QpsBO.builder().apiPath(path).expireSecond(rangStartTime + RANGE).key(key).build());
        }

        return true;
    }

    public static Integer getQpsCacheCount(){
        return QPS_CACHE.size();
    }
}
