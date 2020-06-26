package com.awo.sample.spi;

/**
 * @author: Create by awo
 * @date: 2020/6/25
 * @Discription:
 **/

public class MSpiService implements SpiService {
    @Override
    public void saySpi() {
        System.out.printf("my spi say hello");
    }
}
