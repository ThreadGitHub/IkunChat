package org.chat.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.chat.mapper.ChatMessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * websocket handler
 * @author thread
 * @date 2023/7/28 16:52
 */
@Component
@Scope(value = "prototype")
@Slf4j
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Autowired
    private ChatMessageMapper chatMessageMapper;

    private final String HEART = "ws-heart";

    private static List<String> nameSets = new ArrayList<String>(){{
        this.add("小明");
        this.add("鸡哥");
        this.add("鸡你太美");
        this.add("我家鸽鸽");
        this.add("只因你太美");
        this.add("小黑子");
        this.add("小黑宝");
        this.add("真ikun");
        this.add("坤坤本坤");
        this.add("可爱坤");
        this.add("坤坤的小可爱");
        this.add("坤坤的小娇妻");
        this.add("ikun无敌");
        this.add("ikun万岁");
        this.add("ikun");
        this.add("坤坤");
    }};

    /**
     * 连接context
     */
    public static final Map<String, ChannelHandlerContext> contextMap = new ConcurrentHashMap<>();
    public static final Map<ChannelHandlerContext, String> nameMap = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        String text = textWebSocketFrame.text();
        log.info("接收到消息: " + text);
        // 判断接收内容是否为心跳值
        if(HEART.equals(text)){
            TextWebSocketFrame socketFrame = new TextWebSocketFrame(HEART);
            channelHandlerContext.writeAndFlush(socketFrame);
        } else {
            String alias = nameMap.get(channelHandlerContext);
            if(text != null && text.contains("updateNikeName")){
                String updateNikeName = text.replace("updateNikeName", "");
                nameMap.put(channelHandlerContext,updateNikeName);
                contextMap.remove(alias);
                contextMap.put(updateNikeName,channelHandlerContext);
                this.broadcast(updateNikeName, "用户【"+alias+"】名称变更为【"+updateNikeName+"】");
                return ;
            }

            // 如果不是心跳就把接收到的消息转发给每个客户端
            String message = textWebSocketFrame.text();

            this.broadcast(alias, message);
        }
    }

    /**
     * 获取用户昵称
     * @return
     */
    public String getAlias() {
        int num = new Random().nextInt(nameSets.size());
        String alias = nameSets.get(num);
        while (contextMap.containsKey(alias)) {
            alias = this.getAlias();
        }
        log.info("获取昵称: {}:{}", num, alias);
        return alias;
    }

    /**
     * 用户加入
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        // 获取昵称
        String alias = this.getAlias();
        contextMap.put(alias, ctx);
        nameMap.put(ctx, alias);
        log.info("用户连接");

        /**
         * 异步发送通知
         */
        CompletableFuture.runAsync(()-> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ctx.writeAndFlush(new TextWebSocketFrame("服务器: 分配昵称【"+ alias +"】"));
        });

        log.info(alias + ": 用户连接");
        this.broadcast(alias, "用户连接");

        // 记录消息表
//        chatMessageMapper.insert(alias, "用户连接");
    }

    /**
     * 用户断开
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);

        // 删掉无效连接
        String alias = nameMap.get(ctx);
        contextMap.remove(alias);
        nameMap.remove(ctx);

        // 广播下线通知
        log.info(alias + ": 用户断开");
        this.broadcast(alias, "用户断开连接");

        // 记录消息表
//        chatMessageMapper.insert(alias, "用户断开连接");
    }

    /**
     * 用户非正常断开
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        log.info("用户非正常断开");
        // 记录消息表
        String alias = nameMap.get(ctx);
        chatMessageMapper.insert(alias, "用户非正常断开");
    }

    /**
     * 将消息群发给所有在线的人
     */
    private void broadcast(String alias, String msg) {
        // 记录消息表
        chatMessageMapper.insert(alias, msg);

        msg = alias + ": " + msg;
        String finalMsg = msg;
        contextMap.entrySet().parallelStream().forEach(e -> {
            String name = e.getKey();
            // 不给自己广播
            if (!name.equals(alias)) {
                ChannelHandlerContext ctx = e.getValue();
                if (ctx.channel().isActive()) {
                    ctx.writeAndFlush(new TextWebSocketFrame(finalMsg));
                } else {
                    // 广播时清理掉不活跃的连接
                    contextMap.remove(ctx);
                }
            }
        });
    }
}
