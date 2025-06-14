package com.example.prueba;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class PruebaApplication {

    static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public static void main(String[] args) {
        SpringApplication.run(PruebaApplication.class, args);
    }

}
