package com.zhuolu.cmd.ext.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class CmdInvokeUtil {
    public static List<Method> getMethod(Class<?> type, String methodName, List<Object> params) {
        return Arrays.stream(type.getDeclaredMethods()).filter(method -> {
            // 匹配方法名
            if (!method.getName().equals(methodName)) {
                return false;
            }
            Class<?>[] parameterTypes = method.getParameterTypes();
            return params.size() == parameterTypes.length;
        }).collect(Collectors.toList());
    }

    public static List<Method> getMethod(List<Method> methodList, List<Object> params) {
        return methodList.stream().filter(method -> {
            Class<?>[] parameterTypes = method.getParameterTypes();
            int length = parameterTypes.length;
            for (int i = 0; i < length; i++) {
                if (!isJsonTypeToJavaType(params.get(i), parameterTypes[i])) {
                    return false;
                }
            }
            return true;
        }).collect(Collectors.toList());
    }

    private static boolean isJsonTypeToJavaType(Object param, Class<?> type) {
        if (param == null) {
            return !type.isPrimitive();
        }
        type = toBoxed(type);
        // json的基础类型
        if (param instanceof String) {
            return type == String.class || Date.class.isAssignableFrom(type) || type == Character.class;
        }
        if (param instanceof Number) {
            return Number.class.isAssignableFrom(type) || Date.class.isAssignableFrom(type) || type == Character.class;
        }
        if (param instanceof Boolean) {
            return Boolean.class == type;
        }
        // 集合
        if (param instanceof Collection) {
            Collection<?> collectionParam = (Collection<?>) param;
            // 获取一个非空的元素
            Object o = null;
            for (Object e : collectionParam) {
                if (e != null) {
                    o = e;
                    break;
                }
            }
            if (o == null) {
                return true;
            }
            // 数组
            if (type.isArray()) {
                Class<?> componentType = type.getComponentType();
                return isJsonTypeToJavaType(o, componentType);
            }
            return Collection.class.isAssignableFrom(type);
        }
        if (param instanceof Map) {
            if (Map.class.isAssignableFrom(type)) {
                return true;
            }
            String className = (String) ((Map) param).get("class");
            if (className != null && className.length() > 0) {
                Class<?> clazz = null;
                try {
                    clazz = Class.forName(className);
                } catch (Throwable ignore) {
                }
                return clazz != null && type.isAssignableFrom(clazz);
            }
            return !isJsonPrimitive(type);
        }
        return true;
    }

    public static void main(String[] args) {
        Map<String, Object> m = new HashMap<>();
        System.out.println(isJsonTypeToJavaType(m, String.class));
    }

    public <T extends CharSequence> void fun(List<T> ar) {}

    /**
     * 是否是json的基础类型
     * 包括基础类型及其包装
     * 时间类型
     * 字符串类型
     * @param type
     * @return
     */
    private static boolean isJsonPrimitive(Class<?> type) {
        return
            type.isPrimitive() ||
            Number.class.isAssignableFrom(type) ||
            Boolean.class == type ||
            Character.class == type ||
            CharSequence.class.isAssignableFrom(type) ||
            Date.class.isAssignableFrom(type);
    }

    private static Class<?> toBoxed(Class<?> type) {
        if (type == byte.class) {
            return Byte.class;
        }
        if (type == short.class) {
            return Short.class;
        }
        if (type == int.class) {
            return Integer.class;
        }
        if (type == long.class) {
            return Long.class;
        }
        if (type == float.class) {
            return Float.class;
        }
        if (type == double.class) {
            return Double.class;
        }
        if (type == char.class) {
            return Character.class;
        }
        if (type == boolean.class) {
            return Boolean.class;
        }
        return type;
    }
}
