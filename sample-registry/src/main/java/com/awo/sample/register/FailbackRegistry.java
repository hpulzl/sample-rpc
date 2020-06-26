package com.awo.sample.register;

import com.awo.sample.common.URL;

/**
 * @author: Create by awo
 * @date: 2020/6/11
 * @Discription:
 **/
public abstract class FailbackRegistry implements RegistryService {

    @Override
    public void register(URL url) {

        doRegister(url);
    }

    protected abstract void doRegister(URL url);

    protected abstract void doSubscribe(URL url, NotifyListener listener);


    @Override
    public void subscribe(URL url, NotifyListener listener) {

        doSubscribe(url, listener);
    }
}
