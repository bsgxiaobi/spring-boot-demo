package com.bishugui.websocketNetty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author bi shugui
 * @description 消息处理器
 * @date 2023/9/10 16:30
 */
@Slf4j
@ChannelHandler.Sharable
@Component
public class WebSocketNettyMessageHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    /**
     * 用户通道
     */
    Map<String, ChannelHandlerContext> USER_CHANNEL_MAP = new ConcurrentHashMap<>();

    /**
     * 用户组通道，ChannelGroup详见https://www.cnblogs.com/flydean/p/15906513.html
     */
    Map<String,ChannelGroup> GROUP_CHANNEL_MAP = new ConcurrentHashMap<>();
    /**
     *
     * @param channelHandlerContext 通道上下文
     * @param textWebSocketFrame 文本数据帧
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        log.info("接收到的文本数据帧:{}",textWebSocketFrame.text());
        channelHandlerContext.channel().writeAndFlush(new TextWebSocketFrame("收到你的消息：" +  textWebSocketFrame.text()));
    }

    /**
     * 客户端与服务器建立连接的时候触发
     * @param ctx 通道上下文
     * @throws Exception 异常
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端与服务器建立连接");
        USER_CHANNEL_MAP.put(ctx.channel().id().asLongText(),ctx);
        ctx.channel().writeAndFlush(new TextWebSocketFrame("欢迎来到websocket服务器"));
        super.channelActive(ctx);
    }

    /**
     * 客户端与服务器关闭连接的时候触发
     * @param ctx 通道上下文
     * @throws Exception 异常
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端与服务器关闭连接");
        USER_CHANNEL_MAP.remove(ctx.channel().id().asLongText());
        super.channelInactive(ctx);
    }


    /**
     * 出现异常时触发
     * @param ctx 通道上下文
     * @param cause 捕获的异常
     * @throws Exception 异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("出现异常:{}",cause.getMessage());
        USER_CHANNEL_MAP.remove(ctx.channel().id().asLongText());
        super.exceptionCaught(ctx, cause);
    }
}
