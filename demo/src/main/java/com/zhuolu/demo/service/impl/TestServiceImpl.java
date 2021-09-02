package com.zhuolu.demo.service.impl;

import com.zhuolu.demo.domain.TestBean1;
import com.zhuolu.demo.domain.TestBean2;
import com.zhuolu.demo.service.TestService;
import org.springframework.stereotype.Service;

@Service("testService")
public class TestServiceImpl implements TestService {
    @Override
    public String selectTest(TestBean1 testBean1) {
        return "this is testbean1" + testBean1;
    }

    @Override
    public String selectTest(TestBean2 testBean2) {
        return "this is testbean2" + testBean2;
    }

    @Override
    public String sayHello(String name) {
        if (name == null || name.length() == 0) {
            name = "wenjin";
        }
        return "fuck you " + name;
    }
}
