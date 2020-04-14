package com.awo.sample.common;

import lombok.Data;

/**
 * @author: Create by awo
 * @date: 2020/4/5
 * @Discription: URL契约：参考 http://dubbo.apache.org/zh-cn/blog/introduction-to-dubbo-url.html
 **/
@Data
public class URL {

    /**
     * IP地址
     */
    private String host;

    /**
     * 端口
     */
    private int port;

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 路径
     */
    private String uri;

    public URL(){

    }

    public URL(String host,int port,String serviceName) {
        this.host = host;
        this.port = port;
        this.serviceName = serviceName;
    }

    public String toIdentityString() {
        return null;
    }
}
