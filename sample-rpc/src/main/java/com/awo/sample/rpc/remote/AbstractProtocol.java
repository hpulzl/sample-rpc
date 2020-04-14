package com.awo.sample.rpc.remote;

import com.awo.sample.common.URL;
import com.awo.sample.rpc.*;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author: Create by awo
 * @date: 2020/4/12
 * @Discription:
 **/
public abstract class AbstractProtocol implements Protocol {

    /**
     *  暴露的对象
     */
    private final static Map<String, Exporter<?>> exporterMap = new ConcurrentHashMap<>();

    /**
     * invoker对象
     */
    private final static Set<Invoker<?>> invokerSet = new ConcurrentSkipListSet<>();

    protected ProxyFactory proxyFactory;

    @Override
    public <T> Exporter<T> export(Invoker<T> invoker) {
        final String uri = serviceKey(invoker.getUrl());
        Exporter<?> exporter = exporterMap.get(uri);
        if (exporter != null) {
            return (Exporter<T>) exporter;
        }
        final Runnable runnable = doExport(proxyFactory.getProxy(invoker), invoker.getInterface(), invoker.getUrl());
        exporter = new AbstractExporter(invoker) {
            @Override
            public void unexport() {
                exporterMap.remove(uri);
                if (runnable != null) {
                    try {
                        runnable.run();
                    } catch (Throwable t) {
                       t.printStackTrace();
                    }
                }
            }
        };
        exporterMap.put(uri, exporter);
        return (Exporter<T>) exporter;
    }

    /**
     * 通过不同协议实现暴露方法
     * @param proxy
     * @param anInterface
     * @param url
     * @param <T>
     * @return
     */
    protected abstract <T> Runnable doExport(T proxy, Class<T> anInterface, URL url);

    private String serviceKey(URL url) {
        // 简单点实现，URL解析有些复杂
        return url.getUri();
    }

    @Override
    public <T> Invoker<T> refer(Class<T> type, URL url) {
        final Invoker<T> target = proxyFactory.getInvoker(doRefer(type, url), type, url);
        Invoker<T> invoker = new AbstractInvoker<T>(type, url) {
            @Override
            protected Result doInvoke(Invocation invocation) {
                try {
                    Result result = target.invoke(invocation);
                    return result;
                } catch (RpcException e) {
                    throw e;
                } catch (Throwable e) {
                    throw e;
                }
            }
        };
        invokerSet.add(invoker);
        return invoker;
    }

    /**
     * 通过不同的协议实现引用方法
     * @param type
     * @param url
     * @param <T>
     * @return
     */
    protected abstract <T> T doRefer(Class<T> type, URL url);
}
