package com.zhuolu.cmd.impl.domain;

import java.lang.reflect.Method;

public class InvokeHolder {
    private final Object bean;
    private final Method method;
    private final Object[] args;

    public InvokeHolder(Object bean, Method method, Object[] args) {
        this.bean = bean;
        this.method = method;
        this.args = args;
    }

    public Object getBean() {
        return bean;
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArgs() {
        return args;
    }

    @Override
    public String toString() {
        return method.toString();
    }
}