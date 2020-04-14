package com.awo.sample;

import com.awo.sample.common.URL;
import com.awo.sample.rpc.Exporter;
import com.awo.sample.rpc.Invoker;
import com.awo.sample.rpc.Protocol;
import com.awo.sample.rpc.ProxyFactory;
import com.awo.sample.rpc.proxy.JDKProxyFactory;
import com.awo.sample.rpc.remote.rmi.RmiProtocol;
import org.junit.Test;

/**
 * @author: Create by awo
 * @date: 2020/4/12
 * @Discription:
 **/
public class ProtocolTest {


    @Test
    public void jdkProxyTest() {
        HelloService service = new HelloServiceImpl();
        // 获取代理对象
        ProxyFactory proxyFactory = new JDKProxyFactory();
        // url
        URL url = new URL("127.0.0.1", 9001, "HelloService");
        url.setUri("rmi://127.0.0.1:9001/HelloService");
        // 动态代理方式获取service
        Invoker<HelloService> invoker = proxyFactory.getInvoker(service, HelloService.class, url);
        // 通过代理类生成的对象
        HelloService client = proxyFactory.getProxy(invoker);

        client.sayHello("sample rpc so easy");
    }

    @Test
    public void rmiProtocolTest() {
        HelloService service = new HelloServiceImpl();
        // 获取代理对象
        ProxyFactory proxyFactory = new JDKProxyFactory();
        // 获取远程调用对象
        Protocol protocol = new RmiProtocol(proxyFactory);
        // url
        URL url = new URL("127.0.0.1", 9001, "HelloService");
        url.setUri("rmi://127.0.0.1:9001/HelloService");
        // 动态代理方式获取service
        Invoker<HelloService> invoker = proxyFactory.getInvoker(service, HelloService.class, url);
        // 服务端暴漏对象
        Exporter<HelloService> export = protocol.export(invoker);
        // 调用端引用对象
        Invoker<HelloService> refer = protocol.refer(HelloService.class, url);
        // 通过代理类生成的对象
        HelloService client = proxyFactory.getProxy(refer);

        client.sayHello("sample rpc so easy");

        // 资源释放
        refer.destroy();
        export.unexport();
    }

}
