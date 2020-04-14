package com.awo.sample.rpc;

/**
 * @author: Create by awo
 * @date: 2020/4/6
 * @Discription: rpc调用时参数的抽象
 **/
public interface Invocation {

    String getMethodName();

    Class<?>[] getParameterTypes();

    Object[] getArguments();
}
