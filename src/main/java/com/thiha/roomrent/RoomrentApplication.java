package com.thiha.roomrent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RoomrentApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoomrentApplication.class, args);
	}

}
