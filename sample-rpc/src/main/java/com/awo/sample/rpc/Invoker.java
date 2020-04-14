package com.awo.sample.rpc;

import com.awo.sample.common.URL;

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

    void destroy();

    URL getUrl();

    /**
     * 接口信息
     * @return
     */
    Class<T> getInterface();
}
