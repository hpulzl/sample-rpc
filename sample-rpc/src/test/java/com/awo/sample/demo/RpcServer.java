package com.awo.sample.demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author: Create by awo
 * @date: 2020/5/9
 * @Discription: 用netty模拟RPC服务端解码、响应过程
 * 编码格式：msg:requestId| ==>(消息体:请求id|)
 **/
public class RpcServer {

    public static void main(String[] args) throws InterruptedException {
        // boss
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // worker
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        // 创建业务处理的handler
        final NettyServerHandler serverHandler = new NettyServerHandler();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline p = ch.pipeline();
                    // 设置帧分隔符解码器
                    ByteBuf delimiter = Unpooled.copiedBuffer("|".getBytes());
                    p.addLast(new DelimiterBasedFrameDecoder(1000, delimiter));
                    // 设置消息内容自动转换成String的解码器到管线
                    p.addLast(new StringDecoder());
                    // 设置字符串消息自动进行编码的编码器到管线
                    p.addLast(new StringEncoder());
                    p.addLast(serverHandler);

                    System.out.println("channelPipeline 中的所有channel:" + p.names());
                }
            });

            // 启动服务
            ChannelFuture sync = bootstrap.bind(12800).sync();
            // 等待服务监听套接字关闭
            sync.channel().closeFuture().sync();
        } finally {
            //  关闭
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
