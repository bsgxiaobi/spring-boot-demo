package com.bishugui.websocketNetty;

import cn.hutool.json.JSONUtil;
import com.bishugui.websocketNetty.domain.MessageVO;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

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
    private static Map<String, ChannelHandlerContext> USER_CHANNEL_MAP = new ConcurrentHashMap<>(5000);

    /**
     * 用户组通道，ChannelGroup详见https://www.cnblogs.com/flydean/p/15906513.html
     */
    private static Map<String,ChannelGroup> GROUP_CHANNEL_MAP = new ConcurrentHashMap<>();

    public static Map<String, ChannelHandlerContext> getUserChannelMap() {
        return USER_CHANNEL_MAP;
    }

    public static Map<String, ChannelGroup> getGroupChannelMap() {
        return GROUP_CHANNEL_MAP;
    }

    static AtomicInteger closeCounter = new AtomicInteger(0);
    /**
     *
     * @param channelHandlerContext 通道上下文
     * @param textWebSocketFrame 文本数据帧
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        //log.info("接收到的文本数据帧:{}",textWebSocketFrame.text());
        channelHandlerContext.channel().writeAndFlush(new TextWebSocketFrame("收到你的消息：" +  textWebSocketFrame.text()));
    }

    /**
     * 客户端与服务器建立连接的时候触发
     * @param ctx 通道上下文
     * @throws Exception 异常
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //super.channelActive(ctx);
        //log.info("客户端:{},与服务器建立连接",ctx.channel().id());
        //ctx.writeAndFlush(new TextWebSocketFrame("欢迎来到websocket服务器"));
        USER_CHANNEL_MAP.put(ctx.channel().id().asLongText(),ctx);
    }

    /**
     * 客户端与服务器关闭连接的时候触发
     * @param ctx 通道上下文
     * @throws Exception 异常
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("连接关闭，客户端:{}，关闭总数：{}",ctx.channel().id(), closeCounter.incrementAndGet());
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

    /**
     * 发送消息给所有用户
     * @param msg 消息
     */
    public static Integer sendMsgToAllUser(Object msg){
        if (USER_CHANNEL_MAP.size() == 0) {
            return -1;
        }
        MessageVO messageVO = new MessageVO();
        messageVO.setData(msg);
        messageVO.setCreateDate(new Date());
        messageVO.setType("BROADCAST");
        String jsonStr = JSONUtil.toJsonStr(messageVO);
        AtomicInteger count = new AtomicInteger(0);
        final TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(jsonStr);
        //ByteBuf byteBuf = Unpooled.copiedBuffer(jsonStr, CharsetUtil.UTF_8);
        long startTime = System.currentTimeMillis();
        for (ChannelHandlerContext context : USER_CHANNEL_MAP.values()) {
            context.writeAndFlush(textWebSocketFrame.copy());
            count.getAndIncrement();
        }
        //USER_CHANNEL_MAP.values().forEach(context -> {
            // channel.writeAndFlush()是从当前channel所关联的pipeline的最后一个context开始写回
            //context.channel().writeAndFlush(new TextWebSocketFrame(jsonStr));
            // ChannelHandlerContext.writeAndFlush()是从当前调用writeAndFlush()方法的context开始，沿着出站方向，开始执行写回操作
            //context.writeAndFlush(textWebSocketFrame);
            //count.getAndIncrement();
            // 前端无法解析字节数组，将报错
            //ChannelFuture channelFuture = context.channel().writeAndFlush(byteBuf);
            // 添加监听器
            //ChannelFuture channelFuture = context.channel().writeAndFlush(new TextWebSocketFrame(jsonStr));
            //channelFuture.addListener(new ChannelFutureListener() {
            //    @Override
            //    public void operationComplete(ChannelFuture channelFuture) throws Exception {
            //        try {
            //            if (channelFuture.isSuccess()) {
            //                log.info("消息发送成功");
            //            } else {
            //                log.info("消息发送失败");
            //            }
            //        }catch (Exception e){
            //            log.error("消息发送失败",e);
            //        }
            //    }
            //});
        //});
        log.info("发送消息完成，总数:{}，用时:{}ms", count.get(), System.currentTimeMillis() - startTime);
        return count.get();
    }
}
