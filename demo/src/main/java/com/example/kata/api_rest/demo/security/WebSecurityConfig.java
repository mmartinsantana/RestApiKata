package com.example.kata.api_rest.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

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

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth, DataSource dataSource, PasswordEncoder passwordEncoder) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
				.usersByUsernameQuery("select user_name as username, pass as password, true"
						+ " from app_user where user_name=?")
				.authoritiesByUsernameQuery("select user_name as username, authority"
						+ " from app_user u join app_user_authorities ua on u.id=ua.app_user_id join authority a on ua.authorities_id = a.id where user_name=?");
	}


}