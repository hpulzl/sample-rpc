package com.awo.sample.rpc;

import com.awo.sample.common.URL;

/**
 * @author: Create by awo
 * @date: 2020/4/12
 * @Discription:
 **/
public interface Protocol {

    /**
     *  服务端暴漏接口调用
     * @param invoker
     * @param <T>
     * @return
     */
    <T> Exporter<T> export(Invoker<T> invoker);

    /**
     * 消费端消费接口调用
     * @param type
     * @param url
     * @param <T>
     * @return
     */
    <T> Invoker<T> refer(Class<T> type, URL url);

    /**
     * destroy
     */
    void destroy();
}
