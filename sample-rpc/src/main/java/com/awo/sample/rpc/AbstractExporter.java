package com.awo.sample.rpc;

/**
 * @author: Create by awo
 * @date: 2020/4/12
 * @Discription:
 **/
public abstract class AbstractExporter<T> implements Exporter<T> {

    private Invoker<T> invoker;

    public AbstractExporter(Invoker<T> invoker) {
        this.invoker = invoker;
    }

    @Override
    public Invoker<T> getInvoker() {
        return invoker;
    }
}
