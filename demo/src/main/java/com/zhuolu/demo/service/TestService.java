package com.zhuolu.demo.service;

import java.util.List;

public interface TestService {
    String sayHello(String name);

    String sayHello(List<String> names);
}
