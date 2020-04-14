package com.awo.sample.rpc;

import java.lang.reflect.Method;

/**
 * @author: Create by awo
 * @date: 2020/4/14
 * @Discription:
 **/
public class RpcInvocation implements Invocation {

    private String methodName;

    private Class<?>[] parameterTypes;

    private Object[] arguments;

    private Invoker<?> invoker;

    public RpcInvocation() {
    }

    public RpcInvocation(Method method, Object[] args) {
        this(method.getName(), method.getParameterTypes(), args);
    }

    public RpcInvocation(String methodName, Class<?>[] parameterTypes, Object[] arguments) {
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.arguments = arguments;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }

    public void setInvoker(Invoker<?> invoker) {
        this.invoker = invoker;
    }
}
