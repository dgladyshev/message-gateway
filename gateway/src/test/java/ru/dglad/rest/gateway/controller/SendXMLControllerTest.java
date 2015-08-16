package ru.dglad.rest.gateway.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = {MessageGatewayApplication.class})

public class SendXMLControllerTest {
	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;

	@Autowired
	private MessageService mockMessageService;

	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).defaultRequest(post("/").contentType(
				MediaType.APPLICATION_XML)).build();

		when(mockMessageService.send(any(SendRequest.class))).thenReturn("789687678");
	}

	private static final String CORRECT_REQUEST_USING_TEXT = "<?xml version=\"1.0\" encoding=\"windows-1251\"?>"
			+ "<request><address>9168574273</address>"
			+ "<sender>incognito</sender>"
			+ "<initiator>12</initiator>"
			+ "<requestId>789687678</requestId>"
			+ "<text>Privet</text>"
			+ "</request>";

	private static final String CORRECT_URI = "/message";

	@Test
	public void testSendSuccsess() throws Exception {
		//send json return xml (content type = xml, accept header not set)
		mockMvc.perform(post(CORRECT_URI)
				.content(CORRECT_REQUEST_USING_TEXT))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(content().string("{\"messageId\":\"789687678\"}"));
	}

	private static final String INCORRECT_REQUEST_EMPTY_TEXT = "<?xml version=\"1.0\" encoding=\"windows-1251\"?>"
			+ "<request><address>9168574273</address>"
			+ "<sender>incognito</sender>"
			+ "<initiator>12</initiator>"
			+ "<requestId>789687678</requestId>"
			+ "<text />"
			+ "</request>";

	@Test
	public void testSendEmptyText() throws Exception {
		mockMvc.perform(post(CORRECT_URI)
				.content(INCORRECT_REQUEST_EMPTY_TEXT))
				.andExpect(status().isBadRequest());
	}
}
