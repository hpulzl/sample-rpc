package com.awo.sample.demo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author: Create by awo
 * @date: 2020/5/9
 * @Discription:
 **/
public class TestRpc {

    private static final RpcClient client = new RpcClient();

    public static void main(String[] args) {
        // 异步请求
        CompletableFuture<String> future = client.rpcAsyncCall("hello");

        future.whenComplete((v, t) -> {
            if (t != null) {
                t.printStackTrace();
            } else {
                System.out.println("异步调用: " + v);
            }
        });

        System.out.println("----- async rpc call over");

        try {
            String s = client.rpcSyncCall(" rpc request");
            System.out.println("同步调用:" + s);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
