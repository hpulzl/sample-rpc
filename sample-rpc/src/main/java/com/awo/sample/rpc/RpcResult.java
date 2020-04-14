package com.awo.sample.rpc;

/**
 * @author: Create by awo
 * @date: 2020/4/12
 * @Discription: rpc的返回结果封装
 **/
public class RpcResult implements Result {

    private Object object;

    public RpcResult(Object obj) {
        this.object = obj;
    }

    @Override
    public Object recreate() {
        return object;
    }
}
