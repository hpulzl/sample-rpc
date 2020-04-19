package com.awo.sample.spi;

import org.junit.Test;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author: Create by awo
 * @date: 2020/4/19
 * @Discription:
 **/
public class JavaSPITest {

    @Test
    public void javaSPITest() {
        ServiceLoader<HelloService> serviceLoader = ServiceLoader.load(HelloService.class);
        Iterator<HelloService> iterable = serviceLoader.iterator();
        while (iterable != null && iterable.hasNext()) {
            HelloService next = iterable.next();
            String name = next.getClass().getName();
            System.out.println("class:" + name);
            next.sayHello(" hi ");
        }
    }
}
