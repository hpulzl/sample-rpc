package com.awo.sample.rpc.remote.rmi;

import com.awo.sample.common.URL;
import com.awo.sample.rpc.ProxyFactory;
import com.awo.sample.rpc.remote.AbstractProtocol;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.remoting.rmi.RmiServiceExporter;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationFactory;

import java.rmi.RemoteException;

/**
 * @author: Create by awo
 * @date: 2020/4/13
 * @Discription: rmi协议的远程调用 http://dubbo.apache.org/zh-cn/blog/dubbo-101.html
 **/
public class RmiProtocol extends AbstractProtocol {


    public RmiProtocol(ProxyFactory proxyFactory) {
        super.proxyFactory = proxyFactory;
    }
    @Override
    protected <T> Runnable doExport(T proxy, Class<T> anInterface, URL url) {
        final RmiServiceExporter exporter = new RmiServiceExporter();
        exporter.setRegistryPort(url.getPort());
        exporter.setServiceName(url.getServiceName());
        exporter.setServiceInterface(anInterface);
        exporter.setService(proxy);
        try {
            exporter.afterPropertiesSet();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return new Runnable() {
            @Override
            public void run() {
                try {
                    exporter.destroy();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    protected <T> T doRefer(Class<T> type, URL url) {
        RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
        rmiProxyFactoryBean.setRemoteInvocationFactory(new RemoteInvocationFactory() {
            @Override
            public RemoteInvocation createRemoteInvocation(MethodInvocation methodInvocation) {
                return new RemoteInvocation(methodInvocation);
            }
        });
        rmiProxyFactoryBean.setServiceUrl(url.getUri());
        rmiProxyFactoryBean.setServiceInterface(type);
        rmiProxyFactoryBean.setCacheStub(true);
        rmiProxyFactoryBean.setLookupStubOnStartup(true);
        rmiProxyFactoryBean.setRefreshStubOnConnectFailure(true);
        rmiProxyFactoryBean.afterPropertiesSet();
        return (T) rmiProxyFactoryBean.getObject();
    }

    @Override
    public void destroy() {

    }
}
