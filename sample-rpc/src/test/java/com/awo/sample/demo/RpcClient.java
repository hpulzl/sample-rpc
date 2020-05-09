package com.awo.sample.demo;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author: Create by awo
 * @date: 2020/5/9
 * @Discription:
 **/
public class RpcClient {

    // 连接通道
    private volatile Channel channel;

    private static final AtomicLong REQ_ID = new AtomicLong(1);

    // 启动器
    private Bootstrap bootstrap;

    public RpcClient() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            bootstrap = new Bootstrap();
            final NettyClientHandler clientHandler = new NettyClientHandler();

            bootstrap.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
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
                            p.addLast(clientHandler);
                        }
                    });
            // 发送链接请求，并同步等待链接完成
            ChannelFuture sync = bootstrap.connect("127.0.0.1", 12800).sync();
            if (sync.isDone() && sync.isSuccess()) {
                this.channel = sync.channel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMsg(String msg) {
        channel.writeAndFlush(msg);
    }

    /**
     * 同步请求
     *
     * @param msg
     * @return
     */
    public String rpcSyncCall(String msg) throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = new CompletableFuture<>();
        String reqId = REQ_ID.getAndIncrement() + "";
        String request = generatorFrame(msg, reqId);
        this.sendMsg(request);
        FutureMapUtil.put(reqId, future);
        String result = future.get();
        return result;
    }

    public String generatorFrame(String msg, String reqId) {
        return msg + ":" + reqId + "|";
    }

    /**
     * 异步请求
     *
     * @param msg
     * @return
     */
    public CompletableFuture rpcAsyncCall(String msg) {
        CompletableFuture<String> future = new CompletableFuture<>();
        // 获取请求id
        String reqId = REQ_ID.getAndIncrement() + "";
        // 封装报文结构
        String request = generatorFrame(msg, reqId);
        this.sendMsg(request);
        FutureMapUtil.put(reqId, future);
        return future;
    }

    public void close() {
        if (bootstrap != null) {
            bootstrap.group().shutdownGracefully();
        }

        if (channel != null) {
            channel.close();
        }
    }
}
