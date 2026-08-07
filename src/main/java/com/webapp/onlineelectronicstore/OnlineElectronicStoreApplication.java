package com.webapp.onlineelectronicstore;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class  OnlineElectronicStoreApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(OnlineElectronicStoreApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}




