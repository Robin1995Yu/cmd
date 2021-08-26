package com.zhuolu.demo.service.impl;

import com.zhuolu.demo.service.TestService;
import org.springframework.stereotype.Service;

@Service("testService")
public class TestServiceImpl implements TestService {
    @Override
    public String sayHello(String name) {
        return "hello " + name;
    }
}
