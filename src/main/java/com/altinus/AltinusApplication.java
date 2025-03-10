package com.altinus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class AltinusApplication {

    public static void main(String[] args) {
        SpringApplication.run(AltinusApplication.class, args);
    }

}
