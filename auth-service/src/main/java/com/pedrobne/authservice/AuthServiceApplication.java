package com.pedrobne.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

    @RestController
    @RequestMapping("hello")
    public static class HelloController {
        @RequestMapping
        public String hello() {
            return "Hello World!";
        }
    }

}
