package com.awo.sample.rpc;

import com.awo.sample.common.URL;

/**
 * @author: Create by awo
 * @date: 2020/4/14
 * @Discription:
 **/
public abstract class AbstractProxyInvoker<T> implements Invoker<T> {

    private Class<T> type;

    private URL url;

    private T proxy;

    public AbstractProxyInvoker(Class<T> type, URL url, T proxy) {
        this.type = type;
        this.url = url;
        this.proxy = proxy;
    }

    @Override
    public Result invoke(Invocation invocation) throws RpcException {
        return new RpcResult(doInvoke(proxy, invocation.getMethodName(), invocation.getParameterTypes(), invocation.getArguments()));
    }

    protected abstract Object doInvoke(T proxy, String methodName, Class<?>[] parameterTypes, Object[] arguments);

    @Override
    public void destroy() {

    }

    @Override
    public URL getUrl() {
        return url;
    }

    @Override
    public Class<T> getInterface() {
        return type;
    }
}
