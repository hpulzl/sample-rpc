package com.awo.sample.register;

import com.awo.sample.common.URL;

/**
 * @author: Create by awo
 * @date: 2020/6/11
 * @Discription:
 **/
public abstract class FailbackRegistry implements RegistryService {

//    private static final Map<>

    @Override
    public void register(URL url) {

        doRegister(url);
    }

    protected abstract void doRegister(URL url);


    @Override
    public void subscribe(URL url) {

    }
}
