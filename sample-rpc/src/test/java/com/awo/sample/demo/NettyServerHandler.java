package com.awo.sample.demo;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author: Create by awo
 * @date: 2020/5/9
 * @Discription: sharable
 **/
@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 拼接请求信息
     *
     * @param msg   消息内容
     * @param reqId 请求id
     * @return
     */
    public String generatorFrame(String msg, String reqId) {
        return msg + ":" + reqId + "|";
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) {

        AllChannelHandler.channelRead(() -> {
            String request = (String) msg;
            String reqId = request.split(":")[1];

            String resp = NettyServerHandler.this.generatorFrame("this is result", reqId);

            ctx.channel().writeAndFlush(Unpooled.copiedBuffer(resp.getBytes()));
        });
    }
}
