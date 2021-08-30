package com.zhuolu.cmd.ext.cmd;

import com.alibaba.fastjson.JSON;
import com.zhuolu.cmd.core.CmdRuntime;
import com.zhuolu.cmd.core.entry.cmd.AbstractCmd;
import com.zhuolu.cmd.core.entry.cmd.Cmd;
import com.zhuolu.cmd.core.entry.cmd.iterator.BufferedReaderIterator;
import com.zhuolu.cmd.ext.utils.CmdInvokeUtil;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class InvokeCmd extends AbstractCmd {
    public InvokeCmd(Cmd previous, List<String> param, CmdRuntime cmdRuntime) {
        super("invoke", previous, param, cmdRuntime);
    }

    @Override
    protected void assertParam() {
        if (param == null || param.isEmpty()) {
            throw new IllegalArgumentException("invoke cmd must has one param");
        }
    }

    private boolean flag = true;

    private Object bean;
    private Method method;
    private Object[] args;
    private String result;

    @Override
    protected void init() {
        String invokeString = param.get(0);
        // 处理字符串 分离出beanName methodName paramString 方便后续处理
        int i = invokeString.indexOf('(');
        if (i < 0 || !invokeString.endsWith(")")) {
            throw new IllegalArgumentException("invoke param must case to class.method(params)");
        }
        // 参数列表
        String paramString = invokeString.substring(i + 1, invokeString.length() - 1);
        // bean名字.方法名
        String beanMethodName = invokeString.substring(0, i);
        int cmSplit = invokeString.lastIndexOf('.');
        // bean名字
        String beanName = beanMethodName.substring(0, cmSplit);
        // 方法名
        String methodName = beanMethodName.substring(cmSplit + 1);
        bean = getCmdRuntime().getExportContextUtil().get(beanName);
        if (bean == null) {
            throw new IllegalArgumentException("no such bean:" + beanName);
        }
        Class<?> c = bean.getClass();
        List<Object> paramList;
        try {
            paramList = JSON.parseArray("[" + paramString + "]");
        } catch (Throwable t) {
            throw new IllegalArgumentException(t);
        }
        // 找同名同参数数量的方法
        List<Method> methodList = CmdInvokeUtil.getMethod(c, methodName, paramList);
        if (methodList.isEmpty()) {
            throw new IllegalArgumentException("no such method:" + methodName);
        }
        if (methodList.size() == 1) {
            // 只命中一个
            method = methodList.get(0);
        } else {
            // TODO select
        }
        // 开始构造参数
        args = new Object[paramList.size()];
        try {
            Class<?>[] paramClasses = method.getParameterTypes();
            Type[] paramTypes = method.getGenericParameterTypes();
            for (int j = 0; j < paramList.size(); j++) {
                Class<?> paramClass = paramClasses[j];
                Type paramType = paramTypes[j];
                Object param = paramList.get(j);
                args[j] = CmdInvokeUtil.getParam(param, paramClass, paramType);
            }
        } catch (Throwable t) {
            throw new IllegalArgumentException("no such method:" + methodName);
        }
    }

    @Override
    public void invoke() {
        try {
            Object invoke = method.invoke(bean, args);
            result = Objects.toString(invoke);
        } catch (Throwable e) {
            new IllegalArgumentException(e);
        }
    }

    @Override
    protected Iterator<String> doIterator() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(result.getBytes(StandardCharsets.UTF_8))));
        return new BufferedReaderIterator(reader);
    }
}
