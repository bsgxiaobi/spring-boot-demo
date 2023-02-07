package com.bishugui.demoqps.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author bi shugui
 * @description qps 信息
 * @date 2023/2/7 21:46
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class QpsBO implements Comparable<QpsBO> {
    /**
     * 到期时间 秒
     */
    private Long expireSecond;

    /**
     * 路径
     */
    private String apiPath;

    /**
     * 缓存中的key
     */
    private String key;


    /**
     * 排序
     * @param o the object to be compared.
     * @return 从小到大排序
     */
    @Override
    public int compareTo(QpsBO o) {
        long dif = this.expireSecond - o.expireSecond;
        return dif > 0 ? 1 : (dif < 0 ? -1 : 0);
    }
}
