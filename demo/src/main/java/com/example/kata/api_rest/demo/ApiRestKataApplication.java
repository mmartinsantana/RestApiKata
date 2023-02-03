package com.example.kata.api_rest.demo;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
@ComponentScan("com.example.kata.api_rest.demo.service, " +
		"com.example.kata.api_rest.demo.repository, " +
		"com.example.kata.api_rest.demo.controller, " +
		"com.example.kata.api_rest.demo.security")
public class ApiRestKataApplication {

	@Bean
	public Hibernate5Module hibernateModule() {
		return new Hibernate5Module();
	}

	public static void main(String[] args) {
		SpringApplication.run(ApiRestKataApplication.class, args);
	}

}
