package com.bishugui.websocketNetty;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author bi shugui
 * @description websocket通道初始化
 * @date 2023/9/10 16:16
 */
@Component
public class WebSocketChannelInit extends ChannelInitializer<SocketChannel> {
    @Value("${websocket-config.path}")
    private String websocketPath;
    @Value("${websocket-config.max-frame-size}")
    private Integer maxFrameSize;

    @Resource
    private WebSocketNettyMessageHandler webSocketNettyMessageHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // 添加SSL访问,详见：https://blog.csdn.net/qq_38215042/article/details/125747184；https://blog.csdn.net/yaya_jn/article/details/130410613
        // 直接使用java的websocket仅需要nginx配置了ssl后即可；对netty websocket于是否仅需要nginx配置了ssl后即可，存疑

        // 对http协议的支持
        socketChannel.pipeline().addLast(new HttpServerCodec());
        // 对大数据流的支持,以块的方式来写的处理器,分块向客户端写数据，防止发送大文件时导致内存溢出
        socketChannel.pipeline().addLast(new ChunkedWriteHandler());
        //post请求分三部分. request line / request header / message body
        // HttpObjectAggregator将多个信息转化成单一的request或者response对象
        socketChannel.pipeline().addLast(new HttpObjectAggregator(65535));

        // 添加处理器
        socketChannel.pipeline().addLast(webSocketNettyMessageHandler);

        // 将http协议升级为ws协议. websocket的支持
        //socketChannel.pipeline().addLast(new WebSocketServerProtocolHandler(websocketPath));
        socketChannel.pipeline().addLast(new WebSocketServerProtocolHandler(websocketPath,null,true,maxFrameSize));

        // WebSocket服务器压缩处理程序，前端也需要添加压缩才行，否则报错：RSV != 0 and no extension negotiated, RSV:4
        //socketChannel.pipeline().addLast(new WebSocketServerCompressionHandler());

        // 对客户端，如果在60秒内没有向服务端发送心跳，就主动断开
        socketChannel.pipeline().addLast(new IdleStateHandler(600, 0, 0));

        // 添加字符编码
        socketChannel.pipeline().addLast(new StringEncoder());
        socketChannel.pipeline().addLast(new StringDecoder());
        // 添加字节数组编码
        socketChannel.pipeline().addLast(new ByteArrayEncoder());
        // 通道入站处理程序适配器
        socketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                if(msg instanceof FullHttpRequest fullHttpRequest){
                    String uri = fullHttpRequest.uri();
                    if (!uri.equals(websocketPath)) {
                        // 访问的路径不是 websocket的端点地址，响应404
                        ctx.channel().writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND))
                                .addListener(ChannelFutureListener.CLOSE);
                        return ;
                    }
                }
                super.channelRead(ctx, msg);
            }
        });
    }
}
