package com.zhuolu.cmd.ext.utils;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static com.zhuolu.cmd.ext.utils.CmdInvokeParamUtil.*;

public class CmdInvokeParamUtilTest {

    @Test
    public void getClassLoaderTest() {
    }

    @Test
    public void createInstanceTest() {
    }

    @Test
    public void isPrimitiveTest() {
    }

    @Test
    public void toBoxedClassTest() {
    }

    @Test
    public void packageToPrimitiveTest() {
        assertNull(CmdInvokeParamUtil.packageToPrimitive(null, void.class));
        Object o = new Object();
        assertEquals(packageToPrimitive(o, null), o);
        assertNull(packageToPrimitive(null, null));

        assertEquals(packageToPrimitive("a", char.class), 'a');
        assertEquals(packageToPrimitive("a", Character.class), 'a');

        assertEquals(packageToPrimitive("ENUM1", PackageToPrimitiveTestEnum.class), PackageToPrimitiveTestEnum.ENUM1);
        assertEquals(packageToPrimitive("ENUM2", PackageToPrimitiveTestEnum.class), PackageToPrimitiveTestEnum.ENUM2);

        assertEquals(packageToPrimitive("100", BigInteger.class), new BigInteger("100"));
        assertEquals(packageToPrimitive("100.1234", BigDecimal.class), new BigDecimal("100.1234"));
        short s = 100;
        assertEquals(packageToPrimitive("100", Short.class), s);
        assertEquals(packageToPrimitive("100", short.class), s);
        assertEquals(packageToPrimitive("100", Integer.class), 100);
        assertEquals(packageToPrimitive("100", int.class), 100);
        assertEquals(packageToPrimitive("100", Long.class), 100L);
        assertEquals(packageToPrimitive("100", long.class), 100L);
        assertEquals(packageToPrimitive("100.3", Double.class), 100.3);
        assertEquals(packageToPrimitive("100.3", double.class), 100.3);
        assertEquals(packageToPrimitive("100.3", Float.class), 100.3F);
        assertEquals(packageToPrimitive("100.3", float.class), 100.3F);
        byte b = 100;
        assertEquals(packageToPrimitive("100", Byte.class), b);
        assertEquals(packageToPrimitive("100", byte.class), b);
        assertEquals(packageToPrimitive("true", Boolean.class), true);
        assertEquals(packageToPrimitive("false", Boolean.class), false);

        long currentTimeMillis = System.currentTimeMillis();
        // 精确到秒
        currentTimeMillis = currentTimeMillis / 1000 * 1000;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = simpleDateFormat.format(new Date(currentTimeMillis));
        assertEquals(packageToPrimitive(dateString, Date.class), new Date(currentTimeMillis));
        assertEquals(packageToPrimitive(dateString, java.sql.Date.class), new java.sql.Date(currentTimeMillis));
        assertEquals(packageToPrimitive(dateString, java.sql.Time.class), new java.sql.Time(currentTimeMillis));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(currentTimeMillis));
        assertEquals(packageToPrimitive(dateString, Calendar.class), calendar);

        assertEquals(packageToPrimitive("java.lang.Object", Class.class), Object.class);
        assertTrue(Arrays.equals((char[]) packageToPrimitive("abcd", char[].class), "abcd".toCharArray()));

        assertEquals(packageToPrimitive(100, Short.class), s);
        assertEquals(packageToPrimitive(100, short.class), s);
        assertEquals(packageToPrimitive(100, Integer.class), 100);
        assertEquals(packageToPrimitive(100, int.class), 100);
        assertEquals(packageToPrimitive(100, Long.class), 100L);
        assertEquals(packageToPrimitive(100, long.class), 100L);
        assertEquals(packageToPrimitive(100.3, Double.class), 100.3);
        assertEquals(packageToPrimitive(100.3, double.class), 100.3);
        assertEquals(packageToPrimitive(100.3, Float.class), 100.3F);
        assertEquals(packageToPrimitive(100.3, float.class), 100.3F);
        assertEquals(packageToPrimitive(100, Byte.class), b);
        assertEquals(packageToPrimitive(100, byte.class), b);
        assertEquals(packageToPrimitive(100, BigInteger.class), BigInteger.valueOf(100));
        assertEquals(packageToPrimitive(currentTimeMillis, Date.class), new Date(currentTimeMillis));
        assertEquals(packageToPrimitive(1, Boolean.class), true);
        assertEquals(packageToPrimitive(0, Boolean.class), false);
        List<Integer> integerList = new ArrayList<>(10);
        int[] intArray = new int[10];
        Integer[] integerArray = new Integer[10];
        for (int i = 0; i < 10; i++) {
            integerList.add(i);
            intArray[i] = i;
            integerArray[i] = i;
        }
        assertTrue(Arrays.equals((int[]) packageToPrimitive(integerList, int[].class), intArray));
        assertTrue(Arrays.equals((Integer[]) packageToPrimitive(integerList, Integer[].class), integerArray));
        assertEquals(packageToPrimitive(integerList, ArrayList.class), integerList);
        assertEquals(packageToPrimitive(integerList, LinkedList.class), integerList);
    }

    private enum PackageToPrimitiveTestEnum {
        ENUM1,
        ENUM2
    }
}