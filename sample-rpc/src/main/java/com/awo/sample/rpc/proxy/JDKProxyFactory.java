package com.awo.sample.rpc.proxy;

import com.awo.sample.common.URL;
import com.awo.sample.rpc.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author: Create by awo
 * @date: 2020/4/12
 * @Discription:
 **/
public class JDKProxyFactory implements ProxyFactory {

    @Override
    public <T> T getProxy(final Invoker<T> invoker) throws RpcException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        final Class<T>[] interfaces = new Class[]{invoker.getInterface()};
        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (method.getDeclaringClass() == Object.class) {
                    return method.invoke(invoker, args);
                }
                return invoker.invoke(new RpcInvocation(method, args)).recreate();
            }
        };
        return (T) Proxy.newProxyInstance(classLoader, interfaces, handler);
    }

    @Override
    public <T> Invoker<T> getInvoker(final T proxy, final Class<T> type, final URL url) throws RpcException {
        return new AbstractProxyInvoker<T>(type, url, proxy) {
            @Override
            protected Object doInvoke(T proxy, String methodName, Class<?>[] parameterTypes, Object[] arguments) {
                Method method = null;
                Object invoke = null;
                try {
                    method = proxy.getClass().getMethod(methodName, parameterTypes);
                    invoke = method.invoke(proxy, arguments);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                return invoke;
            }
        };
    }
}
