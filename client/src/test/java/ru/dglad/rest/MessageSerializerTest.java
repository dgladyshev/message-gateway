package ru.dglad.rest;

import org.junit.Test;
import ru.dglad.rest.api.requests.SendRequest;
import ru.dglad.rest.api.responses.ErrorResponse;
import ru.dglad.rest.client.util.MessageSerializer;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class MessageSerializerTest {

	private static final String CORRECT_REQUEST_USING_TEXT =
			"{" +
			"  \"address\" : \"8001234567\"," +
			"  \"sender\" : \"Alex\"," +
			"  \"requestId\" : \"10\"," +
			"  \"text\" : \"Important Message\"" +
			"}";

	@Test
	public void testPOJOtoJSON() throws IOException {
		SendRequest request = new SendRequest();
		request.address("8001234567");
		request.sender("Alex");
		request.requestId("10");
		request.text("Important Message");

		String json = new MessageSerializer().serialize(request);
		assertThat(json, containsString("\"address\":\"8001234567\""));
		assertThat(json, containsString("\"sender\":\"Alex\""));
		assertThat(json, containsString("\"requestId\":\"10\""));
		assertThat(json, containsString("\"text\":\"Important Message\""));
	}

	@Test
	public void testJSONtoPOJO() throws IOException {
		SendRequest request = new SendRequest();
		request.address("8001234567");
		request.sender("Alex");
		request.requestId("10");
		request.text("Important Message");

		SendRequest obj = new MessageSerializer().deserialize(CORRECT_REQUEST_USING_TEXT, SendRequest.class);

		assertEquals(obj.getAddress(),request.getAddress());
		assertEquals(obj.getSender(),request.getSender());
		assertEquals(obj.getRequestId(),request.getRequestId());
		assertEquals(obj.getText(),request.getText());
	}

	@Test
	public void testErrorResponseToJSON() throws IOException {
		ErrorResponse errorResponse = new ErrorResponse(1, true, "crash!");
		String json = new MessageSerializer().serialize(errorResponse);
		assertThat(json, containsString("\"error\":{"));
		assertThat(json, containsString("\"code\":1"));
		assertThat(json, containsString("\"fatal\":true"));
		assertThat(json, containsString("\"message\":\"crash!\""));
	}

}
