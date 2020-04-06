package com.awo.sample.rpc;

import com.awo.sample.common.URL;

/**
 * @author: Create by awo
 * @date: 2020/4/6
 * @Discription: 动态代理接口
 **/
public interface ProxyFactory {

    /**
     * 创建代理对象
     *
     * @param invoker
     * @param <T>
     * @return
     * @throws RpcException
     */
    <T> T getProxy(Invoker<T> invoker) throws RpcException;

    /**
     * 创建invoker
     * @param proxy
     * @param type
     * @param url
     * @param <T>
     * @return
     * @throws RpcException
     */
    <T> Invoker<T> getInvoker(T proxy, Class<T> type, URL url) throws RpcException;
}
