package com.goldengym;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.goldengym.mapper")
public class GoldenGymApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoldenGymApplication.class, args);
    }
}
