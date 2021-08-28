package com.zhuolu.cmd.ext.utils;

import com.zhuolu.cmd.core.entry.cmd.Cmd;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

import static org.junit.Assert.*;

public class CmdInvokeUtilTest<T extends CharSequence> {
    public int[] intArray = {1, 2, 3};
    public List<Integer> integerList = listOf(1, 2, 3);
    public List<String> stringList = listOf("1", "2", "3");
    public List<T> tList = (List<T>) listOf("1", "2", "3");
    public List<List<Integer>> integerListList = listOf(listOf(1, 2, 3), listOf(1, 2, 3), listOf(1, 2, 3));

    @Test
    public void getMethod() {
    }

    @Test
    public void getParam() throws Exception {
        Assert.assertEquals(CmdInvokeUtil.getParam(1, BigInteger.class, BigInteger.class), new BigInteger("1"));
        Assert.assertEquals(CmdInvokeUtil.getParam(1.1, BigDecimal.class, BigDecimal.class), new BigDecimal("1.1"));
        Assert.assertEquals(CmdInvokeUtil.getParam(1, Byte.class, Byte.class), Byte.valueOf((byte) 1));
        Assert.assertEquals(CmdInvokeUtil.getParam(1, Short.class, Integer.class), Short.valueOf((short) 1));
        Assert.assertEquals(CmdInvokeUtil.getParam(1, Integer.class, Integer.class), Integer.valueOf(1));
        Assert.assertEquals(CmdInvokeUtil.getParam(1, Long.class, Long.class), Long.valueOf(1));
        Assert.assertEquals(CmdInvokeUtil.getParam(1.1, Float.class, Float.class), Float.valueOf((float) 1.1));
        Assert.assertEquals(CmdInvokeUtil.getParam(1.1, Double.class, Double.class), Double.valueOf(1.1));
        Assert.assertEquals(CmdInvokeUtil.getParam("a", Character.class, Character.class), Character.valueOf('a'));
        Assert.assertEquals(CmdInvokeUtil.getParam(true, Boolean.class, Boolean.class), Boolean.TRUE);
        Assert.assertEquals(CmdInvokeUtil.getParam(1, Date.class, Date.class), new Date(1));
        Assert.assertEquals(CmdInvokeUtil.getParam(1, java.sql.Date.class, java.sql.Date.class), new java.sql.Date(1));
        Assert.assertEquals(CmdInvokeUtil.getParam(1, Time.class, Time.class), new Time(1));
        Assert.assertEquals(CmdInvokeUtil.getParam(1, Timestamp.class, Timestamp.class), new Timestamp(1));

        Assert.assertEquals(CmdInvokeUtil.getParam(1, byte.class, byte.class), (byte) 1);
        Assert.assertEquals(CmdInvokeUtil.getParam(1, short.class, short.class), (short) 1);
        Assert.assertEquals(CmdInvokeUtil.getParam(1, int.class, int.class), 1);
        Assert.assertEquals(CmdInvokeUtil.getParam(1, long.class, long.class), 1L);
        Assert.assertEquals(CmdInvokeUtil.getParam(1.1, float.class, float.class), (float) 1.1);
        Assert.assertEquals(CmdInvokeUtil.getParam(1.1, double.class, double.class), 1.1);
        Assert.assertEquals(CmdInvokeUtil.getParam("a", char.class, char.class), 'a');
        Assert.assertEquals(CmdInvokeUtil.getParam(true, boolean.class, boolean.class), true);

        String date = "2021-08-01 01:02:03";
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(0L);
        c.set(2021, 7, 1, 1, 2, 3);
        long m = c.getTimeInMillis();
        Assert.assertEquals(CmdInvokeUtil.getParam("abcd", String.class, String.class), "abcd");
        Assert.assertEquals(CmdInvokeUtil.getParam(date, Date.class, Date.class), new Date(m));
        Assert.assertEquals(CmdInvokeUtil.getParam(date, java.sql.Date.class, java.sql.Date.class), new java.sql.Date(m));
        Assert.assertEquals(CmdInvokeUtil.getParam(date, Time.class, Time.class), new Time(m));
        Assert.assertEquals(CmdInvokeUtil.getParam(date, Timestamp.class, Timestamp.class), new Timestamp(m));

        Type integerListListType = CmdInvokeUtilTest.class.getField("integerListList").getGenericType();
        Assert.assertEquals(CmdInvokeUtil.getParam(integerListList, List.class, integerListListType), integerListList);
    }

    private static <T> List<T> listOf(T ... elements) {
        List<T> l = new ArrayList<>(elements.length);
        for (T element : elements) {
            l.add(element);
        }
        return l;
    }

    public void fun(List<? extends CharSequence> l) {}
}