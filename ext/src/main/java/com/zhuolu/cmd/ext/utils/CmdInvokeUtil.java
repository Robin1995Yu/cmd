package com.zhuolu.cmd.ext.utils;

import javax.lang.model.type.ArrayType;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public final class CmdInvokeUtil {
    public static List<Method> getMethod(Class<?> type, String methodName, List<Object> params) {
        return Arrays.stream(type.getDeclaredMethods()).filter(method -> {
            // 匹配方法名
            if (!method.getName().equals(methodName)) {
                return false;
            }
            Class<?>[] parameterTypes = method.getParameterTypes();
            if(params.size() != parameterTypes.length) {
                return false;
            }
            int length = parameterTypes.length;
            for (int i = 0; i < length; i++) {
                if (!isJsonTypeToJavaType(params.get(i), parameterTypes[i])) {
                    return false;
                }
            }
            return true;
        }).collect(Collectors.toList());
    }

    public static Object getParam(Object param, Class<?> paramClass, Type paramType) throws Exception {
        if (param == null) {
            if (!paramClass.isPrimitive()) {
                return null;
            }
            throw new Exception();
        }
        paramClass = toBoxed(paramClass);
        if (param instanceof String) {
            String stringParam = (String) param;
            if (paramClass == Character.class) {
                return stringParam.charAt(0);
            }
            if (CharSequence.class.isAssignableFrom(paramClass)) {
                return stringParam;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dateParam = sdf.parse(stringParam);
            if (paramClass == Date.class) {
                return dateParam;
            }
            if (paramClass == java.sql.Date.class) {
                return new java.sql.Date(dateParam.getTime());
            }
            if (paramClass == Time.class) {
                return new Time(dateParam.getTime());
            }
            if (paramClass == Timestamp.class) {
                return new Timestamp(dateParam.getTime());
            }
            throw new Exception();
        }
        if (param instanceof Number) {
            Number numberParam = (Number) param;
            if (paramClass == Character.class) {
                return (char) numberParam.byteValue();
            }
            if (paramClass == Byte.class) {
                return numberParam.byteValue();
            }
            if (paramClass == Short.class) {
                return numberParam.shortValue();
            }
            if (paramClass == Integer.class) {
                return numberParam.intValue();
            }
            if (paramClass == Long.class) {
                return numberParam.longValue();
            }
            if (paramClass == Float.class) {
                return numberParam.floatValue();
            }
            if (paramClass == Double.class) {
                return numberParam.doubleValue();
            }
            if (paramClass == BigInteger.class) {
                return new BigInteger(numberParam.toString());
            }
            if (paramClass == BigDecimal.class) {
                return new BigDecimal(numberParam.toString());
            }
            if (paramClass == Date.class) {
                return new Date(numberParam.longValue());
            }
            if (paramClass == java.sql.Date.class) {
                return new java.sql.Date(numberParam.longValue());
            }
            if (paramClass == Time.class) {
                return new Time(numberParam.longValue());
            }
            if (paramClass == Timestamp.class) {
                return new Timestamp(numberParam.longValue());
            }
            throw new Exception();
        }
        if (param instanceof Boolean) {
            if (paramClass == Boolean.class) {
                return param;
            }
            throw new Exception();
        }
        boolean isGeneric = paramClass != paramType;
        if (param instanceof Collection) {
            Collection<?> paramCollection = (Collection<?>) param;
            Collection result;
            if (paramClass.isArray()) {
                // 获取元素的类型
                Class<?> componentClass = paramClass.getComponentType();
                Type componentType = componentClass;
                if (isGeneric) {
                    // 获取带泛型信息代元素类型
                    GenericArrayType arrayType = (GenericArrayType) paramType;
                    componentType = arrayType.getGenericComponentType();
                    // 如果是无法确定的类型（如T或？等）
                    if (componentType instanceof TypeVariable || componentType instanceof WildcardType) {
                        componentType = componentClass;
                    }
                }
                Object arrayParam = Array.newInstance(componentClass, paramCollection.size());
                int index = 0;
                for (Object o : paramCollection) {
                    Array.set(arrayParam, index++, getParam(o, componentClass, componentType));
                }
                return arrayParam;
            }
            Class<?> eClass = Object.class;
            Type eType = eClass;
            if (isGeneric) {
                ParameterizedType connectionType = (ParameterizedType) paramType;
                Type connectionGen = connectionType.getActualTypeArguments()[0];
                if (connectionGen instanceof Class) {
                    // 没有泛型
                    eClass = (Class<?>) connectionGen;
                    eType = connectionGen;
                } else if (connectionGen instanceof ParameterizedType) {
                    // 带有泛型的
                    eClass = (Class<?>) ((ParameterizedType) connectionGen).getRawType();
                    eType = connectionGen;
                } else if (connectionGen instanceof TypeVariable) {
                    // T
                    Type[] bounds = ((TypeVariable<?>) connectionGen).getBounds();
                    if (bounds.length > 0) {
                        eClass = (Class<?>) bounds[0];
                        eType = bounds[0];
                    }
                } else if (connectionGen instanceof WildcardType) {
                    // ?
                    Type[] upperBounds = ((WildcardType) connectionGen).getUpperBounds();
                    if (upperBounds.length > 0) {
                        eClass = (Class<?>) upperBounds[0];
                        eType = upperBounds[0];
                    }
                }
            }
            if (paramClass == List.class) {
                result = new ArrayList<>(paramCollection.size());
            } else if (paramClass == Set.class) {
                result = new HashSet(paramCollection.size());
            } else {
                throw new Exception();
            }
            for (Object o : paramCollection) {
                result.add(getParam(o, eClass, eType));
            }
        }
        if (param instanceof Map) {
            // TODO 返回是否为Map
            if (Map.class.isAssignableFrom(paramClass)) {
                // 泛型和非泛型两种 key和value用递归
            } else {
                // 如果有class元素 看看符合不
                // 直接反射来生成 可能需要再写两个方法
            }
        }
        return param;
    }

    private static boolean isJsonTypeToJavaType(Object param, Class<?> type) {
        if (param == null) {
            return !type.isPrimitive();
        }
        type = toBoxed(type);
        // json的基础类型
        if (param instanceof String) {
            return type == String.class || Date.class.isAssignableFrom(type) ||
                    (type == Character.class && ((String) param).length() == 1);
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
