package com.awo.sample.spi;

/**
 * @author: Create by awo
 * @date: 2020/4/19
 * @Discription:
 **/
public class SampleServiceImpl implements HelloService {
    @Override
    public void sayHello(String hello) {
        System.out.printf("sample say " + hello);
    }
}
