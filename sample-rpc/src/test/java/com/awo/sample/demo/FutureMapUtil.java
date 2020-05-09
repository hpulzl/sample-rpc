package com.awo.sample.demo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Create by awo
 * @date: 2020/5/9
 * @Discription:
 **/
public class FutureMapUtil {

    private static final ConcurrentHashMap<String, CompletableFuture> FutureMap = new ConcurrentHashMap<>();

    public static void put(String reqId,CompletableFuture future) {
        FutureMap.put(reqId,future);
    }

    public static CompletableFuture remove(String reqId) {
        CompletableFuture remove = FutureMap.remove(reqId);
        return remove;
    }
}
