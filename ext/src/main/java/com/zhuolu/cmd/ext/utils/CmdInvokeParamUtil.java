package com.zhuolu.cmd.ext.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class CmdInvokeParamUtil {
    private static final String DATE_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss";
    private static final ThreadLocal<SimpleDateFormat> DATE_FORMAT = new ThreadLocal<>();

    public static Object createInstance(Object source, Class<?> type) {
        if (source == null) {
            return null;
        }
        /**
         * JSON的基础类型转换
         * 包括一下几类
         * 除包装类型外的常用数字类型（BigInteger,BigDecimal）
         * 基础类型（byte,short,int,long,float,double,char,boolean）
         * 基础类型的包装类型（Byte,Short,Integer,Long,Float,Double,Boolean,Character）
         * 字符串类型（String）
         * 时间类型（Date,java.sql.Date,Time,Timestamp,LocalDateTime,LocalDate,LocalTime）
         */
        if (isPrimitive(type)) {
            if (source instanceof String) {
                return toJsonPrimitive((String) source, type);
            }
            if (source instanceof Number) {

            }
        }
        return null;
    }

    public static ClassLoader getClassLoader() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = CmdInvokeParamUtil.getClassLoader();
        }
        return classLoader;
    }

    public static Object createInstance0(Object source, Class<?> type) {
        // null
        if (source == null || type == null) {
            return source;
        }
        // enum
        if (type != null && type.isEnum() && source.getClass() == String.class) {
            return Enum.valueOf((Class<Enum>) type, (String) source);
        }
        // JSON primitive
        Class<?> sourceClass = toPrimitiveClass(source.getClass());
        if ((sourceClass.isPrimitive()
                || Number.class.isAssignableFrom(sourceClass)
                || sourceClass == String.class)
                &&  isPrimitive(type)) {

        }
        // map
        if (Arrays.stream(type.getInterfaces()).anyMatch(Map.class::equals)) {
            Map<Object, Object> sourceMap = (Map<Object, Object>) source;
            if (Map.class.equals(type)) {
                return new HashMap<>(sourceMap);
            } else {
                Map<Object, Object> result = (Map<Object, Object>) newInstance(type);
                result.putAll(sourceMap);
                result.remove("class");
                return result;
            }
        }
        // other
        return newInstance(type, source);
    }

    /**
     * 判断是否为json基础类型
     * json基础类型包括
     * java的基础类型
     * 字符串
     * 所有包装类型
     * 所有数字类型
     * 时间类型
     * @param c
     * @return
     */
    public static boolean isPrimitive(Class<?> c) {
        return c.isPrimitive() || c == String.class || c == Boolean.class || c == Character.class
                || Number.class.isAssignableFrom(c) || Date.class.isAssignableFrom(c)
                || c == LocalDateTime.class || c == LocalDate.class || c == LocalTime.class
                || Calendar.class.isAssignableFrom(c);
    }

    public static Class<?> toBoxedClass(Class<?> c) {
        if (c == int.class) {
            c = Integer.class;
        } else if (c == boolean.class) {
            c = Boolean.class;
        } else if (c == long.class) {
            c = Long.class;
        } else if (c == float.class) {
            c = Float.class;
        } else if (c == double.class) {
            c = Double.class;
        } else if (c == char.class) {
            c = Character.class;
        } else if (c == byte.class) {
            c = Byte.class;
        } else if (c == short.class) {
            c = Short.class;
        }
        return c;
    }

    public static Class<?> toPrimitiveClass(Class<?> c) {
        if (c == Integer.class) {
            return int.class;
        } else if (c == Boolean.class) {
            return boolean.class;
        } else if (c == Long.class) {
            return long.class;
        } else if (c == Float.class) {
            return float.class;
        } else if (c == Double.class) {
            return double.class;
        } else if (c == Character.class) {
            return char.class;
        } else if (c == Byte.class) {
            return byte.class;
        } else if (c == Short.class) {
            return short.class;
        }
        return c;
    }

    private static Object toJsonPrimitive(String source, Class<?> type) {
        if (type == String.class) {
            return source;
        }
        if (source.isEmpty()) {
            throw new IllegalArgumentException("empty string can not case to " + type);
        }
        if (type == Byte.class) {
            try {
                return Byte.valueOf(source);
            } catch (Throwable t) {
                if (source.length() > 1) {
                    throw new IllegalArgumentException("string length bigger then 1:" + source);
                }
                return (byte) source.charAt(0);
            }
        }
        if (type == Short.class) {
            return Short.valueOf(source);
        }
        if (type == Integer.class) {
            return Integer.valueOf(source);
        }
        if (type == Long.class) {
            return Long.valueOf(source);
        }
        if (type == Float.class) {
            return Float.valueOf(source);
        }
        if (type == Double.class) {
            return Double.valueOf(source);
        }
        if (type == Character.class) {
            if (source.length() > 1) {
                throw new IllegalArgumentException("string length bigger then 1:" + source);
            }
            return source.charAt(0);
        }
        if (type == Boolean.class) {
            return Boolean.valueOf(source);
        }
        if (type == BigInteger.class) {
            return new BigInteger(source);
        }
        if (type == BigDecimal.class) {
            return new BigDecimal(source);
        }
        if (type == Date.class) {
            return formatDateString(source);
        }
        if (type == java.sql.Date.class) {
            return new java.sql.Date(formatDateString(source).getTime());
        }
        if (type == Time.class) {
            return new Time(formatDateString(source).getTime());
        }
        if (type == Timestamp.class) {
            return new Timestamp(formatDateString(source).getTime());
        }
        if (type == Calendar.class) {
            Calendar c = Calendar.getInstance();
            c.setTime(formatDateString(source));
            return c;
        }
        if (type == LocalDateTime.class || type == LocalTime.class || type == LocalDate.class) {
            LocalDateTime localDateTime = LocalDateTime.parse(source);
            if (type == LocalTime.class) {
                return localDateTime.toLocalTime();
            }
            if (type == LocalDate.class) {
                return localDateTime.toLocalDate();
            }
            return localDateTime;
        }
        return null;
    }

    private static Object toJsonPrimitive(Number source, Class<?> type) {
        if (type == String.class) {
            return source.toString();
        }
        if (type == Byte.class) {
            source.byteValue();
        }
        if (type == Short.class) {
            return source.shortValue();
        }
        if (type == Integer.class) {
            return source.intValue();
        }
        if (type == Long.class) {
            return source.longValue();
        }
        if (type == Float.class) {
            return source.floatValue();
        }
        if (type == Double.class) {
            return source.doubleValue();
        }
        if (type == Character.class) {
            return (char) source.byteValue();
        }
        if (type == Boolean.class) {
            return source.intValue() > 0;
        }
        if (type == BigInteger.class) {
            return new BigInteger(source.toString());
        }
        if (type == BigDecimal.class) {
            return new BigDecimal(source.toString());
        }
        return null;
    }

    public static Object newInstance(Class<?> c, Object arg) {
        if (CmdInvokeParamUtil.isPrimitive(c)) {
//            return packageToPrimitive(arg, c);
        }
        Object result = newInstance(c);
        return setAllFields(c, result, (Map<String, Object>) arg);
    }

    public static Object newInstance(Class<?> c) {
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
                args[i] = CmdInvokeParamUtil.getDefaultValue(parameterTypes[i]);
            }
            try {
                result = constructor.newInstance(args);
            } catch (Throwable e) {
                throw new IllegalArgumentException("class " + c.getName() + " do not has any public construct");
            }
        }
        return result;
    }

    public static Object getDefaultValue(Class<?> c) {
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

    private static Object setAllFields(Class<?> c, Object target, Map<String, Object> source) {
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
                            fieldValue = newInstance(m.getParameterTypes()[0], fieldValue);
                            m.invoke(target, fieldValue);
                        } catch (Throwable e) {
                            new IllegalArgumentException(fieldValue + "can not case to " + m.getParameterTypes()[0]);
                        }
                    }
                });
        return target;
    }

    private static Date formatDateString(String source) {
        try {
            Long time = Long.valueOf(source);
            return new Date(time);
        } catch (Throwable t) {
            SimpleDateFormat format = DATE_FORMAT.get();
            if (format == null) {
                format = new SimpleDateFormat(DATE_FORMAT_STRING);
                DATE_FORMAT.set(format);
            }
            try {
                return format.parse(source);
            } catch (ParseException e) {
                throw new IllegalArgumentException("source is not case to " + DATE_FORMAT_STRING + ":" + source);
            }
        }
    }
}
