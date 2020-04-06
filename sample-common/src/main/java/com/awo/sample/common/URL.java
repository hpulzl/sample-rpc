package com.awo.sample.common;

/**
 * @author: Create by awo
 * @date: 2020/4/5
 * @Discription: 通用路径
 **/
public class URL {

    /**
     * IP地址
     */
    private String host;

    /**
     * 端口
     */
    private String port;

    /**
     * 服务名称
     */
    private String serviceName;


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
