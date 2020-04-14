package com.awo.sample.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: Create by awo
 * @date: 2020/4/14
 * @Discription:
 **/
@Data
public class Hello implements Serializable {

    private String name;

    private String hi;
}
