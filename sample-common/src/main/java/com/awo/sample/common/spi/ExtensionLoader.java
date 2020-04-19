package com.awo.sample.common.spi;

/**
 * @author: Create by awo
 * @date: 2020/4/19
 * @Discription: 扩展类
 * @see: http://dubbo.apache.org/zh-cn/blog/introduction-to-dubbo-spi.html
 * @see: http://dubbo.apache.org/zh-cn/blog/introduction-to-dubbo-spi-2.html
 **/
public class ExtensionLoader<T> {

    /**
     * 通过接口，获取具体的扩展类实例
     * @param type
     * @param <T>
     * @return
     */
    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type){
        return null;
    }
}
