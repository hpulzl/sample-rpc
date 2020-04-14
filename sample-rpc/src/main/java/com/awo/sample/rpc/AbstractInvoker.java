package com.awo.sample.rpc;

import com.awo.sample.common.URL;

/**
 * @author: Create by awo
 * @date: 2020/4/12
 * @Discription:
 **/
public abstract class AbstractInvoker<T> implements Invoker<T> {

    private final Class<T> type;

    private final URL url;

    public AbstractInvoker(Class<T> type, URL url) {
        this.type = type;
        this.url = url;
    }

    @Override
    public URL getUrl() {
        return url;
    }

    @Override
    public Class<T> getInterface() {
        return type;
    }

    @Override
    public void destroy() {

    }

    @Override
    public Result invoke(Invocation inv) throws RpcException {
        RpcInvocation invocation = (RpcInvocation) inv;
        invocation.setInvoker(this);
        return doInvoke(invocation);
    }

    protected abstract Result doInvoke(Invocation invocation);


}
