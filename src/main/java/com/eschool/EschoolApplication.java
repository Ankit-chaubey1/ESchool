package com.eschool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EschoolApplication {

	public static void main(String[] args) {
		SpringApplication.run(EschoolApplication.class, args);
	}

}
