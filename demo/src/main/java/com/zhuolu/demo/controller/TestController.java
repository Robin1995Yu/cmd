package com.zhuolu.demo.controller;

import com.zhuolu.demo.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private TestService testService;

    @GetMapping("/sayHello")
    public String sayHello(@RequestParam String name) {
        return name + "你是狗吧";
    }

    @GetMapping("/a")
    public String a() {
        return "<center><h1>王张伟为什么还不退群？</h1></center>";
    }

    @GetMapping("/b")
    public String b() {
        return "<center><h1>刘严严是聪明蛋</h1></center>";
    }
}
