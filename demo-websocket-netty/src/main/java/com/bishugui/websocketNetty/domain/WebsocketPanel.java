package com.bishugui.websocketNetty.domain;

import lombok.Data;

import java.util.Map;

/**
 * @author bi shugui
 * @description
 * @date 2023/9/11 21:30
 */
@Data
public class WebsocketPanel {
    private Integer userChannelCount;

    private String userChannelMemory;

    private String groupChannelMemory;

    private Map<String,Integer> groupChannelCount;
}
