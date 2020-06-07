package com.awo.sample.demo;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.CompletableFuture;

/**
 * @author: Create by awo
 * @date: 2020/5/9
 * @Discription:
 **/
@ChannelHandler.Sharable
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.printf("已经与服务端建立连接");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.printf("已经与服务端取消连接");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        AllChannelHandler.channelRead(() -> {
            CompletableFuture future = FutureMapUtil.remove(((String) msg).split(":")[1]);
            if (null != future) {
                future.complete(((String) msg).split(":")[0]);
            }
        });
    }
}
