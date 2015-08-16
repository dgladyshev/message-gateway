package ru.dglad.rest.gateway.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEventListener implements ApplicationListener<AbstractAuthenticationEvent> {

	private static final Logger log = LoggerFactory.getLogger(AuthenticationEventListener.class);

	@Override
	public void onApplicationEvent(AbstractAuthenticationEvent authenticationEvent) {
		if (authenticationEvent instanceof InteractiveAuthenticationSuccessEvent) {
			// ignores to prevent duplicate logging with AuthenticationSuccessEvent
			return;
		}
		Authentication authentication = authenticationEvent.getAuthentication();
		String message = "Login attempt with username: "
				+ authentication.getName()
				+ " Success: "
				+ authentication.isAuthenticated()
				+ " Authorities: "
				+ authentication.getAuthorities()
				+ " Details: "
				+ authentication.getDetails();
		log.debug(message);
	}

}