package com.awo.sample.register;

import com.awo.sample.common.URL;

/**
 * @author: Create by awo
 * @date: 2020/4/5
 * @Discription: 服务注册接口
 **/
public interface RegistryService {

    /**
     * 服务注册
     * @param url
     */
    void register(URL url);

    /**
     * 订阅服务
     * @param url
     */
    void subscribe(URL url);
}
