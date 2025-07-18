package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/usr/test")
public class TestController {

    // 1) 간단 문자열 리턴
    @GetMapping("/hello")
    public String hello() {
        return "Hello, Test!";
    }

    // 2) 샘플 JSON 배열 리턴
    @GetMapping("/users")
    public List<Map<String, Object>> listUsers() {
        List<Map<String, Object>> users = new ArrayList<>();

        Map<String, Object> u1 = new HashMap<>();
        u1.put("id", 1);
        u1.put("name", "Alice");
        users.add(u1);

        Map<String, Object> u2 = new HashMap<>();
        u2.put("id", 2);
        u2.put("name", "Bob");
        users.add(u2);

        return users;
    }
}
