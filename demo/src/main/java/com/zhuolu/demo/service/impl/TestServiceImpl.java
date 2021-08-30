package com.zhuolu.demo.service.impl;

import com.zhuolu.demo.service.TestService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("testService")
public class TestServiceImpl implements TestService {
    @Override
    public String sayHello(String name) {
        return "fuck you " + name;
    }

    @Override
    public String sayHello(List<String> names) {
        StringBuilder sb = new StringBuilder();
        for (String name : names) {
            sb.append(sayHello(name)).append("\n");
        }
        return sb.toString();
    }
}
