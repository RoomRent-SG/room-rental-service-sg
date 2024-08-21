package com.thiha.roomrent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import jakarta.validation.Validator;

@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableAsync
public class RoomrentApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoomrentApplication.class, args);
	}

	@Bean
    LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    Validator getValidator() {
        return new LocalValidatorFactoryBean();
    }
}
