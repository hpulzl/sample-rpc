package com.awo.sample.common.spi;

/**
 * @author: Create by awo
 * @date: 2020/6/7
 * @Discription:
 **/
@SPI
public interface ExtensionFactory {

    /**
     * 获取扩展类
     * @param type
     * @param name
     * @param <T>
     * @return
     */
    <T> T getExtension(Class<T> type, String name);
}
