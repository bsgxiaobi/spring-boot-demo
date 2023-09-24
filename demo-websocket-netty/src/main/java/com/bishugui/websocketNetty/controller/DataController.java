package com.bishugui.websocketNetty.controller;

import com.bishugui.websocketNetty.WebSocketNettyMessageHandler;
import com.bishugui.websocketNetty.domain.WebsocketPanel;
import io.netty.channel.group.ChannelGroup;
import org.apache.lucene.util.RamUsageEstimator;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * @author bi shugui
 * @description 数据
 * @date 2023/9/11 0:36
 */
@RestController
@RequestMapping("/data")
public class DataController {
    @GetMapping("/test")
    public Mono<String> test(){
        return Mono.just("test");
    }

    @GetMapping("/info")
    public Mono<WebsocketPanel> getInfo(){
        WebsocketPanel websocketPanel = new WebsocketPanel();
        websocketPanel.setUserChannelCount(WebSocketNettyMessageHandler.USER_ID_CHANNEL_MAP.size());
        //websocketPanel.setUserChannelMemory(RamUsageEstimator.humanSizeOf(WebSocketNettyMessageHandler.getUserChannelMap()));
        //long size = RamUsageEstimator.sizeOfMap(WebSocketNettyMessageHandler.getUserChannelMap());
        //websocketPanel.setUserChannelMemory(String.valueOf(size) + "字节");
        //long groupChannelSize = RamUsageEstimator.sizeOfMap(WebSocketNettyMessageHandler.getGroupChannelMap());
        //websocketPanel.setGroupChannelMemory(String.valueOf(groupChannelSize) + "字节");
        //Map<String,Integer> groupChannelCountMap = new HashMap<>(WebSocketNettyMessageHandler.getGroupChannelMap().size());
        //for (Map.Entry<String, ChannelGroup> channelGroupEntry : WebSocketNettyMessageHandler.getGroupChannelMap().entrySet()) {
        //    groupChannelCountMap.put(channelGroupEntry.getKey(),channelGroupEntry.getValue().size());
        //}
        //websocketPanel.setGroupChannelCount(groupChannelCountMap);
        return Mono.just(websocketPanel);
    }

    @PostMapping("/sendMsgToAll")
    public Mono<Integer> sendMsgToAll(@RequestParam String msg){
        return Mono.just(WebSocketNettyMessageHandler.sendMsgToAllUser(msg));
    }
}
