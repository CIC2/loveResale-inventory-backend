package com.resale.homeflyinventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class VsoInventoryBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(VsoInventoryBackendApplication.class, args);
    }
}


