package com.awo.sample;

import com.awo.sample.common.URL;
import com.awo.sample.register.zk.CuratorZookpeerImpl;
import com.awo.sample.register.zk.ZookeeperRegistry;
import com.awo.sample.register.zk.ZookpeerClient;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class ZookpeerTest
{
    @Test
    public void registerTest() {
        URL url = new URL();
        url.setUri("/sample/");
        ZookeeperRegistry zookeeperRegistry = new ZookeeperRegistry();
        zookeeperRegistry.register(url);

    }
}
