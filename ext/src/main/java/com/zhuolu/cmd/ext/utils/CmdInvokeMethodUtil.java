package com.zhuolu.cmd.ext.utils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CmdInvokeMethodUtil {
    public static List<Method> matchMethod(Class<?> c, String methodName, List<Object> paramList) {
        Method[] declaredMethods = c.getDeclaredMethods();
        return Arrays.stream(declaredMethods).filter(m -> {
            if (!m.getName().equals(methodName)) {
                return false;
            }
            Class<?>[] parameterTypes = m.getParameterTypes();
            if (parameterTypes.length != paramList.size()) {
                return false;
            }
            for (int i = 0; i < paramList.size(); i++) {
                Class<?> paramType = parameterTypes[i];
                Object paramValue = paramList.get(i);
                if (paramValue == null) {
                    if (paramType.isPrimitive()) {
                        return false;
                    }
                    continue;
                }
                // 是否为可以直接出现在json中的类型
                // 我直接抄的dubbo telnet中invoke方法的源码
                // 哎 就是玩
                if (CmdInvokeParamUtil.isPrimitive(paramValue.getClass())) {
                    paramType = CmdInvokeParamUtil.toBoxedClass(paramType);
                    if (paramValue instanceof String && paramType.isEnum()) {
                        continue;
                    }
                    if (!paramType.isInstance(paramValue)) {
                        return false;
                    }
                } else if (paramValue instanceof Map) {
                    String typeName = (String) ((Map<?, ?>) paramValue).get("class");
                    if (typeName != null && typeName.length() > 0) {
                        try {
                            Class<?> clazz = CmdInvokeParamUtil.getClassLoader().loadClass(typeName);
                            if (!paramType.isAssignableFrom(clazz)) {
                                return false;
                            }
                        } catch (ClassNotFoundException e) {
                            throw new IllegalArgumentException("no such class:" + typeName);
                        }
                    } else {
                        return true;
                    }
                } else if (paramValue instanceof Collection) {
                    if (!paramType.isArray() && !paramType.isAssignableFrom(paramValue.getClass())) {
                        return false;
                    }
                } else {
                    if (!paramType.isAssignableFrom(paramValue.getClass())) {
                        return false;
                    }
                }
            }
            return true;
        }).collect(Collectors.toList());
    }

}
