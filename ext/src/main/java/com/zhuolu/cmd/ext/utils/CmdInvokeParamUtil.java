package com.zhuolu.cmd.ext.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static ClassLoader getClassLoader() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = CmdInvokeParamUtil.getClassLoader();
        }
        return classLoader;
    }

    public static void main(String[] args) {
        System.out.println(int[].class.isPrimitive());
    }

    public static Object createInstance(Object source, Class<?> type, Type gType) {
        // null
        if (source == null) {
            return null;
        }
        // enum
        if (type != null && type.isEnum() && source.getClass() == String.class) {
            return Enum.valueOf((Class<Enum>) type, (String) source);
        }
        // json primitive
        if (type.isPrimitive()) {
            type = toBoxedClass(type);
        }
        if (isPrimitive(type) && isPrimitive(source.getClass())) {
            return packageToPrimitive(source, type);
        }
        // array
        // map
        // collection
        // other

        List<Class<?>> interfaces = Arrays.stream(type.getInterfaces()).collect(Collectors.toList());
        if (interfaces.contains(Map.class)) {

        }
        return null;
    }

    public static boolean isPrimitive(Class<?> cls) {
        return cls.isPrimitive() || cls == String.class || cls == Boolean.class || cls == Character.class
                || Number.class.isAssignableFrom(cls) || Date.class.isAssignableFrom(cls);
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

    public static Object packageToPrimitive(Object value, Class<?> type) {
        if (value == null || type == null || type.isAssignableFrom(value.getClass())) {
            return value;
        }

        if (value instanceof String) {
            String string = (String) value;
            if (char.class.equals(type) || Character.class.equals(type)) {
                if (string.length() != 1) {
                    throw new IllegalArgumentException(String.format("CAN NOT convert String(%s) to char!" +
                            " when convert String to char, the String MUST only 1 char.", string));
                }
                return string.charAt(0);
            }
            if (type.isEnum()) {
                return Enum.valueOf((Class<Enum>) type, string);
            }
            if (type == BigInteger.class) {
                return new BigInteger(string);
            }
            if (type == BigDecimal.class) {
                return new BigDecimal(string);
            }
            if (type == Short.class || type == short.class) {
                return new Short(string);
            }
            if (type == Integer.class || type == int.class) {
                return new Integer(string);
            }
            if (type == Long.class || type == long.class) {
                return new Long(string);
            }
            if (type == Double.class || type == double.class) {
                return new Double(string);
            }
            if (type == Float.class || type == float.class) {
                return new Float(string);
            }
            if (type == Byte.class || type == byte.class) {
                return new Byte(string);
            }
            if (type == Boolean.class || type == boolean.class) {
                return Boolean.valueOf(string);
            }
            if (type == Date.class || type == java.sql.Date.class || type == java.sql.Timestamp.class
                    || type == java.sql.Time.class || type == Calendar.class) {
                try {
                    Date date = new SimpleDateFormat(DATE_FORMAT).parse(string);
                    if (type == java.sql.Date.class) {
                        return new java.sql.Date(date.getTime());
                    }
                    if (type == java.sql.Timestamp.class) {
                        return new java.sql.Timestamp(date.getTime());
                    }
                    if (type == java.sql.Time.class) {
                        return new java.sql.Time(date.getTime());
                    }
                    if (type == Calendar.class) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        return calendar;
                    }
                    return date;
                } catch (ParseException e) {
                    throw new IllegalStateException("Failed to parse date " + value + " by format "
                            + DATE_FORMAT + ", cause: " + e.getMessage(), e);
                }
            }
            if (type == java.time.LocalDateTime.class) {
                if (string == null || string.isEmpty()) {
                    return null;
                }
                return LocalDateTime.parse(string);
            }
            if (type == java.time.LocalDate.class) {
                if (string == null || string.isEmpty()) {
                    return null;
                }
                return LocalDate.parse(string);
            }
            if (type == java.time.LocalTime.class) {
                if (string == null || string.isEmpty()) {
                    return null;
                }
                return LocalDateTime.parse(string).toLocalTime();
            }
            if (type == Class.class) {
                try {
                    return getClassLoader().loadClass(string);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
            if (char[].class.equals(type)) {
                // Process string to char array for generic invoke
                // See
                // - https://github.com/apache/dubbo/issues/2003
                int len = string.length();
                char[] chars = new char[len];
                string.getChars(0, len, chars, 0);
                return chars;
            }
        }
        if (value instanceof Number) {
            Number number = (Number) value;
            if (type == byte.class || type == Byte.class) {
                return number.byteValue();
            }
            if (type == short.class || type == Short.class) {
                return number.shortValue();
            }
            if (type == int.class || type == Integer.class) {
                return number.intValue();
            }
            if (type == long.class || type == Long.class) {
                return number.longValue();
            }
            if (type == float.class || type == Float.class) {
                return number.floatValue();
            }
            if (type == double.class || type == Double.class) {
                return number.doubleValue();
            }
            if (type == BigInteger.class) {
                return BigInteger.valueOf(number.longValue());
            }
            if (type == BigDecimal.class) {
                return BigDecimal.valueOf(number.doubleValue());
            }
            if (type == Date.class) {
                return new Date(number.longValue());
            }
            if (type == boolean.class || type == Boolean.class) {
                return 0 != number.intValue();
            }
        }
        if (value instanceof Collection) {
            Collection collection = (Collection) value;
            if (type.isArray()) {
                int length = collection.size();
                Object array = Array.newInstance(type.getComponentType(), length);
                int i = 0;
                for (Object item : collection) {
                    Array.set(array, i++, item);
                }
                return array;
            }
            if (!type.isInterface()) {
                try {
                    Collection result = (Collection) type.newInstance();
                    result.addAll(collection);
                    return result;
                } catch (Throwable ignored) {
                }
            }
            if (type == List.class) {
                return new ArrayList<Object>(collection);
            }
            if (type == Set.class) {
                return new HashSet<Object>(collection);
            }
        }
        if (value.getClass().isArray() && Collection.class.isAssignableFrom(type)) {
            int length = Array.getLength(value);
            Collection collection;
            if (!type.isInterface()) {
                try {
                    collection = (Collection) type.newInstance();
                } catch (Throwable e) {
                    collection = new ArrayList<Object>(length);
                }
            } else if (type == Set.class) {
                collection = new HashSet<Object>(Math.max((int) (length/.75f) + 1, 16));
            } else {
                collection = new ArrayList<Object>(length);
            }
            for (int i = 0; i < length; i++) {
                collection.add(Array.get(value, i));
            }
            return collection;
        }
        return value;
    }
}
