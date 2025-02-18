package com.icia.rmate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.icia.rmate.dto")
public class RMateApplication {

    public static void main(String[] args) {
        SpringApplication.run(RMateApplication.class, args);
    }

}
