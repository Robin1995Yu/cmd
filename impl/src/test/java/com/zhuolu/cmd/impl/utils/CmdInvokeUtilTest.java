package com.zhuolu.cmd.impl.utils;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Type;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CmdInvokeUtilTest {

    @Test
    public void getMethod() throws NoSuchMethodException {
        Class<TestClass> type = TestClass.class;
        List<Object> paramPrimitive = Collections.singletonList(1);
        Assert.assertEquals(CmdInvokeUtil.getMethod(type, "primitive", paramPrimitive).get(0), type.getMethod("primitive", int.class));
        List<Object> paramT = Collections.singletonList(new HashMap<>());
        Assert.assertEquals(CmdInvokeUtil.getMethod(type, "bean", paramT).get(0), type.getMethod("bean", TestBean.class));
        Assert.assertEquals(CmdInvokeUtil.getMethod(type, "bean", paramT).get(1), type.getMethod("bean", Map.class));
        List<Object> paramList = Collections.singletonList(Collections.singletonList("abcd"));
        Assert.assertEquals(CmdInvokeUtil.getMethod(type, "list", paramList).get(0), type.getMethod("list", List.class));
        List<Object> paramListT = Collections.singletonList(Collections.singletonList(new HashMap<>()));
        Assert.assertEquals(CmdInvokeUtil.getMethod(type, "listT", paramListT).get(0), type.getMethod("listT", List.class));
        Assert.assertEquals(CmdInvokeUtil.getMethod(type, "listW", paramListT).get(0), type.getMethod("listW", List.class));
    }

    @Test
    public void getParam() throws Exception {
        Assert.assertEquals(CmdInvokeUtil.getParam(1, byte.class, byte.class), Byte.valueOf((byte) 1));
        Assert.assertEquals(CmdInvokeUtil.getParam(1, short.class, short.class), Short.valueOf((short) 1));
        Assert.assertEquals(CmdInvokeUtil.getParam(1, int.class, int.class), Integer.valueOf(1));
        Assert.assertEquals(CmdInvokeUtil.getParam(1, long.class, long.class), Long.valueOf(1L));
        Assert.assertEquals(CmdInvokeUtil.getParam(1.1, float.class, float.class), Float.valueOf(1.1F));
        Assert.assertEquals(CmdInvokeUtil.getParam(1.1, double.class, double.class), Double.valueOf(1.1));
        Assert.assertEquals(CmdInvokeUtil.getParam(10, char.class, char.class), Character.valueOf((char) 10));

        Assert.assertEquals(CmdInvokeUtil.getParam(1, Byte.class, Byte.class), Byte.valueOf((byte) 1));
        Assert.assertEquals(CmdInvokeUtil.getParam(1, Short.class, Short.class), Short.valueOf((short) 1));
        Assert.assertEquals(CmdInvokeUtil.getParam(1, Integer.class, Integer.class), Integer.valueOf(1));
        Assert.assertEquals(CmdInvokeUtil.getParam(1, Long.class, Long.class), Long.valueOf(1L));
        Assert.assertEquals(CmdInvokeUtil.getParam(1.1, Float.class, Float.class), Float.valueOf(1.1F));
        Assert.assertEquals(CmdInvokeUtil.getParam(1.1, Double.class, Double.class), Double.valueOf(1.1));
        Assert.assertEquals(CmdInvokeUtil.getParam(10, Character.class, Character.class), Character.valueOf((char) 10));
        long l = System.currentTimeMillis();
        Assert.assertEquals(CmdInvokeUtil.getParam(l, Date.class, Date.class), new Date(l));
        Assert.assertEquals(CmdInvokeUtil.getParam(l, java.sql.Date.class, java.sql.Date.class), new java.sql.Date(l));
        Assert.assertEquals(CmdInvokeUtil.getParam(l, Time.class, Time.class), new Time(l));
        Assert.assertEquals(CmdInvokeUtil.getParam(l, Timestamp.class, Timestamp.class), new Timestamp(l));

        Assert.assertEquals(CmdInvokeUtil.getParam("a", char.class, char.class), Character.valueOf('a'));
        Assert.assertEquals(CmdInvokeUtil.getParam("abcd", String.class, String.class), "abcd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(0);
        calendar.set(2020, 0, 1, 1, 1, 1);
        Assert.assertEquals(CmdInvokeUtil.getParam("2020-01-01 01:01:01", Date.class, Date.class), new Date(calendar.getTimeInMillis()));
        Assert.assertEquals(CmdInvokeUtil.getParam("2020-01-01 01:01:01", java.sql.Date.class, java.sql.Date.class), new java.sql.Date(calendar.getTimeInMillis()));
        Assert.assertEquals(CmdInvokeUtil.getParam("2020-01-01 01:01:01", Time.class, Time.class), new Time(calendar.getTimeInMillis()));
        Assert.assertEquals(CmdInvokeUtil.getParam("2020-01-01 01:01:01", Timestamp.class, Timestamp.class), new Timestamp(calendar.getTimeInMillis()));
        Assert.assertEquals(CmdInvokeUtil.getParam("ONE", TestEnum.class, TestEnum.class), TestEnum.ONE);

        Assert.assertEquals(CmdInvokeUtil.getParam(true, boolean.class, boolean.class), Boolean.TRUE);
        Assert.assertEquals(CmdInvokeUtil.getParam(true, Boolean.class, Boolean.class), Boolean.TRUE);

        Type listTType = TestClass.class.getMethod("listT", List.class).getGenericParameterTypes()[0];
        Assert.assertEquals(CmdInvokeUtil.getParam(Collections.singletonList(new HashMap()), List.class, listTType), Collections.singletonList(new TestBean()));
        Type listWType = TestClass.class.getMethod("listW", List.class).getGenericParameterTypes()[0];
        Assert.assertEquals(CmdInvokeUtil.getParam(Collections.singletonList(new HashMap()), List.class, listWType), Collections.singletonList(new TestBean()));
        Type mapStringTestBeanType = TestClass.class.getMethod("map", Map.class).getGenericParameterTypes()[0];
        Map<String, Map<String, Object>> mapParam = new HashMap<>();
        mapParam.put("test", new HashMap<>());
        Map<String, TestBean> mapResult = new HashMap<>();
        mapResult.put("test", new TestBean());
        Assert.assertEquals(CmdInvokeUtil.getParam(mapParam, Map.class, mapStringTestBeanType), mapResult);
        Type mapTType = TestClass.class.getMethod("mapT", Map.class).getGenericParameterTypes()[0];
        Assert.assertEquals(CmdInvokeUtil.getParam(mapParam, Map.class, mapTType), mapResult);
    }

    public static class TestClass {
        public void primitive(int i) {}

        public <T extends TestBean> void bean(T t) {}

        public void bean(Map<String, Object> m) {}

        public void list(List<String> l) {}

        public <T extends TestBean> void listT(List<T> l) {}

        public void listW(List<? extends TestBean> l) {}

        public void map(Map<String, TestBean> m) {}

        public <T extends TestBean> void mapT(Map<String, T> m) {}
    }

    public static class TestBean {
        private Integer id;
        private String name;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestBean testBean = (TestBean) o;
            return id == testBean.id && Objects.equals(name, testBean.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name);
        }
    }

    public enum TestEnum {
        ONE,
        TWO
    }
}