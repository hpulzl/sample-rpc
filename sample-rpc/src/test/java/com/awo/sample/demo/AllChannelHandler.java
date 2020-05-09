package com.awo.sample.demo;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: Create by awo
 * @date: 2020/5/9
 * @Discription:
 **/
public class AllChannelHandler {

    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(8,8,1,
            TimeUnit.MINUTES,new SynchronousQueue<Runnable>(),new ThreadPoolExecutor.CallerRunsPolicy());

    public static void channelRead(Runnable r) {
        executor.execute(r);
    }

    public static void shutdown() {
        executor.shutdown();
    }
}
