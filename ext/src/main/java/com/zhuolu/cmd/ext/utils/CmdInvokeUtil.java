package com.zhuolu.cmd.ext.utils;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
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
        if (Object.class == paramClass) {
            return param;
        }
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
            Type eType = Object.class;
            if (isGeneric) {
                ParameterizedType connectionType = (ParameterizedType) paramType;
                Type connectionGen = connectionType.getActualTypeArguments()[0];
                Type[] types = toClassAndType(connectionGen);
                eClass = (Class<?>) types[0];
                eType = types[1];
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
            Map<?, ?> mapParam = (Map<?, ?>) param;
            if (Map.class.isAssignableFrom(paramClass)) {
                // 泛型和非泛型两种 key和value用递归
                Class<?> keyClass = Object.class;
                Type keyType = Object.class;
                Class<?> valueClass = Object.class;
                Type valueType = Object.class;
                if (isGeneric) {
                    ParameterizedType parameterizedType = (ParameterizedType) paramType;
                    Type key = parameterizedType.getActualTypeArguments()[0];
                    Type value = parameterizedType.getActualTypeArguments()[1];
                    Type[] keyTypes = toClassAndType(key);
                    Type[] valueTypes = toClassAndType(value);
                    keyClass = (Class<?>) keyTypes[0];
                    keyType = keyTypes[1];
                    valueClass = (Class<?>) valueTypes[0];
                    valueType = valueTypes[1];
                }
                Map resultMap = new HashMap();
                for (Map.Entry<?, ?> entry : mapParam.entrySet()) {
                    resultMap.put(
                            getParam(entry.getKey(), keyClass, keyType),
                            getParam(entry.getValue(), valueClass, valueType));
                }
                return resultMap;
            } else {
                String c = (String) mapParam.get("class");
                if (c != null && c.length() > 0) {
                    try {
                        Class<?> clz = Class.forName(c);
                        if (!paramClass.isAssignableFrom(clz)) {
                            throw new Exception();
                        }
                        paramClass = clz;
                    } catch (Throwable t) {
                        throw new Exception();
                    }
                }
                Object result = newInstance(paramClass);
                Method[] methods = paramClass.getMethods();
                for (Method method : methods) {
                    String fieldName = method.getName();
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if (fieldName.length() >= 4 && fieldName.startsWith("set") && parameterTypes.length >= 1) {
                        // 将setter转换为fieldName
                        fieldName = fieldName.substring(3);
                        fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
                        Class<?> fieldClass = parameterTypes[0];
                        Type fieldType = method.getGenericParameterTypes()[0];
                        Object field = mapParam.get(fieldName);
                        if (field != null) {
                            try {
                                Object[] setArgs = new Object[parameterTypes.length];
                                setArgs[0] = getParam(field, fieldClass, fieldType);
                                method.invoke(result, setArgs);
                            } catch (Throwable ignore) {}
                        }
                    }
                }
                return result;
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

    private static Type[] toClassAndType(Type genType) {
        Class<?> resultClass = Object.class;
        Type resultType = Object.class;
        if (genType instanceof Class) {
            // 没有泛型
            resultClass = (Class<?>) genType;
            resultType = genType;
        } else if (genType instanceof ParameterizedType) {
            // 带有泛型的
            resultClass = (Class<?>) ((ParameterizedType) genType).getRawType();
            resultType = genType;
        } else if (genType instanceof TypeVariable) {
            // T
            Type[] bounds = ((TypeVariable<?>) genType).getBounds();
            if (bounds.length > 0) {
                resultClass = (Class<?>) bounds[0];
                resultType = bounds[0];
            }
        } else if (genType instanceof WildcardType) {
            // ?
            Type[] upperBounds = ((WildcardType) genType).getUpperBounds();
            if (upperBounds.length > 0) {
                resultClass = (Class<?>) upperBounds[0];
                resultType = upperBounds[0];
            }
        }
        Type[] result = new Type[2];
        result[0] = resultClass;
        result[1] = resultType;
        return result;
    }

    private static Object newInstance(Class<?> clazz) throws Exception {
        int modifiers = clazz.getModifiers();
        if (Modifier.isInterface(modifiers) || Modifier.isAbstract(modifiers)) {
            if (Modifier.isInterface(modifiers)) {

            } else if (Modifier.isAbstract(modifiers)) {

            }
            return null;
        } else {
            try {
                return clazz.newInstance();
            } catch (Throwable t) {
                for (Constructor<?> constructor : clazz.getConstructors()) {
                    try {
                        Class<?>[] parameterTypes = constructor.getParameterTypes();
                        int length = parameterTypes.length;
                        Object[] params = new Object[length];
                        for (int i = 0; i < length; i++) {
                            params[i] = getDefaultValue(parameterTypes[i]);
                        }
                        return constructor.newInstance(params);
                    } catch (Throwable ignore) {
                    }
                }
                throw new Exception();
            }
        }
    }

    private static Object getDefaultValue(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            if (boolean.class == clazz) {
                return true;
            }
            if (char.class == clazz) {
                return Character.MIN_VALUE;
            }
            Long zero = 0L;
            if (byte.class == clazz) {
                return zero.byteValue();
            }
            if (short.class == clazz) {
                zero.shortValue();
            }
            if (int.class == clazz) {
                zero.intValue();
            }
            if (long.class == clazz) {
                zero.longValue();
            }
        }
        return null;
    }
}
