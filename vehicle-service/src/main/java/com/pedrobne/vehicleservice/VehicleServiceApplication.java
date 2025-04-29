package com.pedrobne.vehicleservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class VehicleServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(VehicleServiceApplication.class, args);
    }

    @RestController
    @RequestMapping("ola")
    public static class OlaController {
        @RequestMapping
        public String hello() {
            return "Olá, Mundo!";
        }
    }

}
