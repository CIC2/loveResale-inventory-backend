package com.resale.resaleinventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class LoveResaleBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoveResaleBackendApplication.class, args);
    }
}


