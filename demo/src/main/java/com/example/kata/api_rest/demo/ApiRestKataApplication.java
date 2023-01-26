package com.example.kata.api_rest.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
@ComponentScan("com.example.kata.api_rest.demo.service, " +
		"com.example.kata.api_rest.demo.repository, " +
		"com.example.kata.api_rest.demo.controller")
public class ApiRestKataApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiRestKataApplication.class, args);
	}

}
