package com.system.seogenix;

import com.system.seogenix.services.AiService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SeOgenixApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeOgenixApplication.class, args);
    }


    @Bean
    CommandLineRunner testAi (AiService aiService){
        return args -> {
            var title = "How to build scalable website using nodeJs";
            System.out.println("sending.... ");
            System.out.println(aiService.generateResponse(title).block());
        };
    }
}
