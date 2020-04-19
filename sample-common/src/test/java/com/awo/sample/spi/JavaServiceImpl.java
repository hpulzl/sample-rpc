package com.awo.sample.spi;

/**
 * @author: Create by awo
 * @date: 2020/4/19
 * @Discription:
 **/
public class JavaServiceImpl implements HelloService {
    @Override
    public void sayHello(String hello) {
        System.out.printf("java say " + hello);
    }
}
