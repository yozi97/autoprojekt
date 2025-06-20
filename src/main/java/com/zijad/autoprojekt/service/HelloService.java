package com.zijad.autoprojekt.service;

import org.springframework.stereotype.Service;
@Service
public class HelloService {
    public String sayHello(){
        return "Hello from the service!";
    }
}
