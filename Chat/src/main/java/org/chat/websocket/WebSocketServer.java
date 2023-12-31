package org.chat.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * websocket server
 * @author thread
 * @date 2023/7/28 16:49
 */
@Slf4j
@Configuration
public class WebSocketServer {
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void start() {
        EventLoopGroup masterEventLoopGroup = new NioEventLoopGroup();
        EventLoopGroup slaveEventLoopGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        ChannelFuture channelFuture = bootstrap.group(masterEventLoopGroup, slaveEventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new HttpServerCodec());
                        pipeline.addLast(new HttpObjectAggregator(1024 * 1024));

                        pipeline.addLast(new WebSocketServerProtocolHandler("/"));

                        // 在30分钟之内收不到消息自动断开
                        pipeline.addLast(new ReadTimeoutHandler(30, TimeUnit.MINUTES));
                        pipeline.addLast(applicationContext.getBean(WebSocketHandler.class));
                    }
                })
                .bind(8888); 
        try {
            channelFuture.sync();
            log.info("连接WebSocket服务器成功...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
