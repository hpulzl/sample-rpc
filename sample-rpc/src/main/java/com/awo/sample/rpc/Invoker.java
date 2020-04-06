package com.awo.sample.rpc;

/**
 * @author: Create by awo
 * @date: 2020/4/6
 * @Discription: rpc调用时触发这个类 T调用的service
 **/
public interface Invoker<T> {

    /**
     * 进行远程调用的动作
     * @param invocation
     * @return
     * @throws RpcException
     */
    Result invoke(Invocation invocation) throws RpcException;
}
