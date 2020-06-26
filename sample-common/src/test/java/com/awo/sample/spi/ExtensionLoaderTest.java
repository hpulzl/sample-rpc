package com.awo.sample.spi;

import com.awo.sample.common.spi.ExtensionLoader;
import org.junit.Test;

/**
 * @author: Create by awo
 * @date: 2020/6/25
 * @Discription:
 **/
public class ExtensionLoaderTest {

    @Test
    public void getExtensionLoader() {
        SpiService my = ExtensionLoader.getExtensionLoader(SpiService.class).getExtension("my");
        my.saySpi();
        SpiService he = ExtensionLoader.getExtensionLoader(SpiService.class).getExtension("he");
        he.saySpi();
    }
}
