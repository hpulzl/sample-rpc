package com.awo.sample.spi;

/**
 * @author: Create by awo
 * @date: 2020/6/25
 * @Discription:
 **/

public class HSpiService implements SpiService {
    @Override
    public void saySpi() {
        System.out.printf("he spi say hello");
    }
}
