package com.awo.sample;

import com.awo.sample.model.Hello;

/**
 * @author: Create by awo
 * @date: 2020/4/12
 * @Discription:
 **/
public interface HelloService {

    void sayHello(String str);

    Hello sayObj(Hello hello);
}
