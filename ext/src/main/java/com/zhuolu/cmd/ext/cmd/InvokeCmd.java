package com.zhuolu.cmd.ext.cmd;

import com.alibaba.fastjson.JSON;
import com.zhuolu.cmd.core.CmdRuntime;
import com.zhuolu.cmd.core.entry.cmd.AbstractCmd;
import com.zhuolu.cmd.core.entry.cmd.Cmd;
import com.zhuolu.cmd.ext.utils.CmdInvokeMethodUtil;
import com.zhuolu.cmd.ext.utils.CmdInvokeParamUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    protected void init() {
        String invokeString = param.get(0);
        int i = invokeString.indexOf('(');
        if (i < 0 || !invokeString.endsWith(")")) {
            throw new IllegalArgumentException("invoke param must case to class.method(params)");
        }
        String paramString = invokeString.substring(i + 1, invokeString.length() - 1);
        String beanMethodName = invokeString.substring(0, i);
        int cmSplit = invokeString.lastIndexOf('.');
        String beanName = beanMethodName.substring(0, cmSplit);
        String methodName = beanMethodName.substring(cmSplit + 1);

        Object bean = getCmdRuntime().getExportContextUtil().get(beanName);
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
        List<Method> methodList = CmdInvokeMethodUtil.matchMethod(c, methodName, paramList);
        if (methodList.isEmpty()) {
            throw new IllegalArgumentException("no such method");
        } else if (methodList.size() > 1) {
            String s = methodList.stream().map(m -> {
                String methodString = m.getName();
                methodString += Arrays.stream(m.getParameterTypes()).map(Class::getSimpleName).collect(Collectors.joining(",", "(", ")"));
                return methodString;
            }).collect(Collectors.joining(","));
            throw new IllegalArgumentException("can't has more overwrite method, please use 'class' field to case method type:" + s);
        }
        this.bean = bean;
        method = methodList.get(0);
        Class<?>[] types = method.getParameterTypes();
        Object[] args = new Object[paramList.size()];
        for (int index = 0; index < paramList.size(); index++) {
            Class<?> type = types[i];
            Object param = paramList.get(i);
        }
    }



    private Object newInstance(Class<?> c, Object arg) {
        if (CmdInvokeParamUtil.isPrimitive(c)) {
            return arg;
        }
        Object result = newInstance(c);
        return setAllFields(c, result, (Map<String, Object>) arg);
    }

    private Object newInstance(Class<?> c) {
        Object result;
        try {
            result = c.newInstance();
        } catch (Throwable t) {
            Constructor<?>[] constructors = c.getConstructors();
            if (constructors.length <= 0) {
                throw new IllegalArgumentException("class " + c.getName() + " do not has any public construct");
            }
            Constructor<?> constructor = constructors[0];
            constructor.setAccessible(true);
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            Object[] args = new Object[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                args[i] = getDefaultValue(parameterTypes[i]);
            }
            try {
                result = constructor.newInstance(args);
            } catch (Throwable e) {
                throw new IllegalArgumentException("class " + c.getName() + " do not has any public construct");
            }
        }
        return result;
    }

    private Object setAllFields(Class<?> c, Object target, Map<String, Object> source) {
        Method[] methods = c.getMethods();
        Arrays.stream(methods)
                .filter(m -> m.getName().startsWith("set")
                        && m.getName().length() >= 4
                        && m.getParameterTypes().length > 0)
                .forEach(m ->
        {
            String fieldName = m.getName().substring(3);
            fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
            Object fieldValue = source.get(fieldName);
            if (fieldValue != null) {
                m.setAccessible(true);
                try {
                    m.invoke(target, fieldValue);
                } catch (Throwable e) {
                    new IllegalArgumentException(fieldValue + "can not case to " + m.getParameterTypes()[0]);
                }
            }
        });
        return target;
    }

    private Object getDefaultValue(Class<?> c) {
        if ("char".equals(c.getName())) {
            return Character.MIN_VALUE;
        }
        if ("boolean".equals(c.getName())) {
            return false;
        }
        if ("byte".equals(c.getName())) {
            return (byte) 0;
        }
        if ("short".equals(c.getName())) {
            return (short) 0;
        }
        return c.isPrimitive() ? 0 : null;
    }
}
