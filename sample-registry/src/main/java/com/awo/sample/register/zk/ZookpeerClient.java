package com.awo.sample.register.zk;

/**
 * @author: Create by awo
 * @date: 2020/6/26
 * @Discription: zk的操作类
 **/
public interface ZookpeerClient {

    void create(String path,boolean ephemeral);

    void delete(String path);
}
