package com.awo.sample.common.spi.impl;

import com.awo.sample.common.spi.ExtensionFactory;
import com.awo.sample.common.spi.ExtensionLoader;
import com.awo.sample.common.spi.SPI;

/**
 * @author: Create by awo
 * @date: 2020/6/7
 * @Discription:
 **/
public class SPIExtensionFactory implements ExtensionFactory {

    @Override
    public <T> T getExtension(Class<T> type, String name) {
        if (type.isInterface() && type.isAnnotationPresent(SPI.class)) {
            ExtensionLoader<T> loader = ExtensionLoader.getExtensionLoader(type);
            if (!loader.getSupportedExtensions().isEmpty()) {
                return loader.getAdaptiveExtension();
            }
        }
        return null;
    }
}
