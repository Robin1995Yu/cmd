package com.zhuolu.cmd.impl.domain;

import com.zhuolu.cmd.impl.factory.ResultCmdFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

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

    public String invoke(ResultCmdFactory resultCmdFactory) throws InvocationTargetException, IllegalAccessException {
        Object result = method.invoke(bean, args);
        String resultString = "result:\t" + result;
        if (result != null) {
            String key = resultCmdFactory.newKey();
            resultCmdFactory.addResult(key, result);
            resultString += "\nplease use result get \"" + key + "\" to get result object";
        }
        return resultString;
    }

    @Override
    public String toString() {
        return method.toString();
    }
}