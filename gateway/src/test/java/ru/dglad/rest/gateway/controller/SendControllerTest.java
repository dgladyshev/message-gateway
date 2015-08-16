package ru.dglad.rest.gateway.controller;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.dglad.rest.api.requests.SendRequest;
import ru.dglad.rest.gateway.MessageGatewayApplication;
import ru.dglad.rest.gateway.service.MessageService;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = {MessageGatewayApplication.class})
public class SendControllerTest {

	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;

	@Autowired
	private MessageService mockMessageService;

	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).defaultRequest(post("/").contentType(MediaType.APPLICATION_JSON)).build();

		when(mockMessageService.send(any(SendRequest.class))).thenReturn("abracadabra");
	}

	private static final String CORRECT_REQUEST = "{" +
			"  \"address\" : \"8001234567\"," +
			"  \"sender\" : \"Alex\"," +
			"  \"requestId\" : \"10\"," +
			"  \"text\" : \"Important Message\"" +
			"}";

	private static final String INCORRECT_REQUEST = "{" +
			"  \"address\" : \"8001234567\"," +
			"  \"text\" : \"Important Message\"" +
			"}";

	private static final String CORRECT_URI = "/message";

	@Test
	public void testSendSuccsess() throws Exception {

		mockMvc.perform(post(CORRECT_URI).content(CORRECT_REQUEST))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(content().string("{\"messageId\":\"abracadabra\"}"));
	}

	@Test
	public void testSendValidation() throws Exception {
		mockMvc.perform(post(CORRECT_URI).content("{}"))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(content().string(Matchers.containsString("\"code\":202,\"fatal\":true")));

		final String withoutText = CORRECT_REQUEST.replaceAll(",\\s*\"text\" : \"Important Message\"", "");
		mockMvc.perform(post(CORRECT_URI).content(withoutText))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(content().string(Matchers.containsString("\"code\":202,\"fatal\":true")));

		final String invalidAddress = INCORRECT_REQUEST.replace("8001234567", "1234567c89");
		mockMvc.perform(post(CORRECT_URI).content(invalidAddress))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(content().string(Matchers.containsString("\"code\":202,\"fatal\":true")));
	}

	@Test
	public void testDeliveryStatus() throws Exception{
		this.mockMvc.perform(get("/message/101")).andExpect(status().isOk());
	}

	@Configuration
	@Profile("testing")
	public static class BeanConfiguration {

		@Bean
		public MessageService messageSendService() {
			return Mockito.mock(MessageService.class);
		}
	}
}
