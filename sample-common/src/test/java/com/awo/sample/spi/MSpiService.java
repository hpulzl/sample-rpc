package com.awo.sample.spi;

import lombok.Data;

/**
 * @author: Create by awo
 * @date: 2020/6/25
 * @Discription:
 **/
@Data
public class MSpiService implements SpiService {

    private String name;

    private String age;

    @Override
    public void saySpi() {
        System.out.printf("my spi say hello");
    }
}
