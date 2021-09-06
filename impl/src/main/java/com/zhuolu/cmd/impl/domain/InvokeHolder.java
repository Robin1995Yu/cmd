package com.zhuolu.cmd.impl.domain;

import com.alibaba.fastjson.JSON;
import com.zhuolu.cmd.impl.factory.ResultCmdFactory;

import java.lang.reflect.InvocationTargetException;
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

    public String invoke(ResultCmdFactory resultCmdFactory, boolean isResult) throws InvocationTargetException, IllegalAccessException {
        method.setAccessible(true);
        Object result = method.invoke(bean, args);
        String resultString = "result:\t" + JSON.toJSONString(result);
        if (result != null && isResult) {
            String key = resultCmdFactory.newKey();
            resultCmdFactory.addResult(key, result);
            resultString += "\nplease use cmd \"result get " + key + "\" to get result object";
        }
        return resultString;
    }

    @Override
    public String toString() {
        return method.toString();
    }
}