package com.awo.sample.register;

import com.awo.sample.common.URL;

import java.util.List;

/**
 * @author: Create by awo
 * @date: 2020/6/26
 * @Discription:
 **/
public interface NotifyListener {

    /**
     * 观察者模式，通知节点变更
     * @param list
     */
    void notify(List<URL> list);
}
