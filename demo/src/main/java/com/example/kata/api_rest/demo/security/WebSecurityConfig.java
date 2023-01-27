package com.example.kata.api_rest.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * https://www.baeldung.com/spring-security-login
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				// h2 console: 	http://localhost:8080/h2-console/login.do
				.headers(headers -> headers.frameOptions().disable())

				.csrf()
				.disable()

				.authorizeRequests()
				.antMatchers("/api/**")
				.hasRole("REST")

				.antMatchers("/h2-console/**", "/login*")
				.permitAll()

				.anyRequest()
				.authenticated()

				.and()

				.formLogin()
				//.loginPage("login.html")
				.loginProcessingUrl("/perform_login")
				.defaultSuccessUrl("/homepage.html", true)
				.failureUrl("/login.html")
				//.failureHandler(authenticationFailureHandler())

				.and()

				.logout()
				.logoutUrl("/perform_logout")
				.deleteCookies("JSESSIONID")
				//.logoutSuccessHandler(logoutSuccessHandler());

				.and()
				.httpBasic();

		;
		return http.build();
		// ...
	}

	@Bean
	public InMemoryUserDetailsManager userDetailsService() {
		UserDetails user1 = User.withUsername("user1")
				.password(passwordEncoder().encode("user1Pass"))
				.roles("USER")
				.build();
		UserDetails rest = User.withUsername("rest")
				.password(passwordEncoder().encode("restPass"))
				.roles("REST")
				.build();
		return new InMemoryUserDetailsManager(user1, rest);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}