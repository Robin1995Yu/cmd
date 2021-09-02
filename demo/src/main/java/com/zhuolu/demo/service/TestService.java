package com.zhuolu.demo.service;

import com.zhuolu.demo.domain.TestBean1;
import com.zhuolu.demo.domain.TestBean2;

public interface TestService {
    String selectTest(TestBean1 testBean1);
    String selectTest(TestBean2 testBean2);
    String sayHello(String name);
    Runnable getRunnable(String s);
}
