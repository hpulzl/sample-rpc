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
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        AllChannelHandler.channelRead(() -> {
            CompletableFuture future = FutureMapUtil.remove(((String) msg).split(":")[1]);
            if (null != future) {
                future.complete(((String) msg).split(":")[0]);
            }
        });
    }
}
