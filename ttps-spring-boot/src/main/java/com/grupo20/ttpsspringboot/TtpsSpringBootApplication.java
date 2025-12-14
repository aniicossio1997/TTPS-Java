package com.grupo20.ttpsspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean; // <--- Importante
import org.springframework.web.client.RestTemplate; // <--- Importante
@SpringBootApplication
public class TtpsSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(TtpsSpringBootApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
