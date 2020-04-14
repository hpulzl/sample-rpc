package com.awo.sample.rpc;

/**
 * @author: Create by awo
 * @date: 2020/4/12
 * @Discription:
 **/
public interface Exporter<T> {


    /**
     * 获取曝露的类
     * @return
     */
    Invoker<T> getInvoker();
    
    void unexport();
}
