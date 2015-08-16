package ru.dglad.rest.gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;

	private final String GET_USER =
			"select username, pwd_hash, enabled from some_table where username = ?";

	private final String GET_ROLES_FOR_USER =
			"select username, role from some_table where username = ?";

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests()
				.antMatchers("/message/**").access("hasRole('ALLOW_MESSAGE_SENDING')")
				.anyRequest().authenticated()
				.and()
				.csrf().disable()
				.httpBasic();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		BCryptPasswordEncoder encoder = passwordEncoder();
		auth.jdbcAuthentication().dataSource(dataSource)
				.usersByUsernameQuery(GET_USER)
				.authoritiesByUsernameQuery(GET_ROLES_FOR_USER)
				.passwordEncoder(encoder);
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10);
	}

}

