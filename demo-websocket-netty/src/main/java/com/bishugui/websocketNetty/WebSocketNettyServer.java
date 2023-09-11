package com.bishugui.websocketNetty;

import cn.hutool.core.thread.ThreadUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jdk.jfr.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author bi shugui
 * @description websocket-netty服务
 * 实现DisposableBean 在容器销毁前会调用destroy 方法进行线程组的关闭
 * @date 2023/9/10 16:00
 */
@Slf4j
@Component
public class WebSocketNettyServer implements DisposableBean {

    @Value("${websocket-config.port}")
    private Integer websocketPort;

    @Resource
    private WebSocketChannelInit webSocketChannelInit;

    /**
     * boss线程组
     */
    private EventLoopGroup bossGroup;

    /**
     * 工作线程组
     */
    private EventLoopGroup workGroup;

    @PostConstruct
    public void start(){
        // 必须使用新线程启动，否则主线程将一直被阻塞，现象：所有接口均不能访问
        ThreadUtil.execAsync(()->{
            bossGroup = new NioEventLoopGroup();
            workGroup = new NioEventLoopGroup();
            // 创建启动助手
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler())
                    .localAddress(websocketPort)
                    .childHandler(webSocketChannelInit)
                    .childOption(ChannelOption.SO_KEEPALIVE,true);

            try {
                // 改为同步操作，否则无法获取到Future对象，会报错
                ChannelFuture sync = serverBootstrap.bind().sync();
                log.info("websocket netty 服务启动成功");
                // 作用：阻塞main函数继续往下执行，防止finally的语句块被触发
                ChannelFuture channelFuture = sync.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }finally {
                //优雅停机
                bossGroup.shutdownGracefully();
                workGroup.shutdownGracefully();
            }
        },true);

    }

    @Override
    public void destroy() throws Exception {
        //close();
    }

    public void close() {
        //if(bossGroup != null){
        //    bossGroup.close();
        //}
        //if(workGroup != null){
        //    workGroup.close();
        //}
    }
}
