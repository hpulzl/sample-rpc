package com.awo.sample.register.zk;

import com.awo.sample.common.URL;
import com.awo.sample.register.FailbackRegistry;
import com.awo.sample.register.NotifyListener;

/**
 * @author: Create by awo
 * @date: 2020/6/11
 * @Discription: zk服务注册中心
 **/
public class ZookeeperRegistry extends FailbackRegistry {

    @Override
    protected void doRegister(URL url) {
        // 1. 连接zk节点
        ZookpeerClient client = new CuratorZookpeerImpl(url);
        // 2. 将url注册到zk
        client.create(url.getUri(), false);
    }

    @Override
    protected void doSubscribe(URL url, NotifyListener listener) {
    }
}
