package ru.dglad.rest.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@EnableAutoConfiguration
public class MessageGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessageGatewayApplication.class, args);
	}
}
