package com.awo.sample;

/**
 * @author: Create by awo
 * @date: 2020/4/12
 * @Discription:
 **/
public class HelloServiceImpl implements HelloService {

    @Override
    public void sayHello(String str) {
        System.out.printf("say " + str);
    }
}
