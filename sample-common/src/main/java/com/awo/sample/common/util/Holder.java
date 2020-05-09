package com.awo.sample.common.util;

/**
 * @author: Create by awo
 * @date: 2020/4/19
 * @Discription: 保证数据一致性
 **/
public class Holder<T> {

    private volatile T value;

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}
