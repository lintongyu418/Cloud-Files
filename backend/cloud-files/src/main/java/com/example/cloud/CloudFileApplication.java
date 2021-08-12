package com.example.cloud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan("com.example")
@MapperScan("com.example.cloud.mappers")
public class CloudFileApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudFileApplication.class, args);
    }
}
