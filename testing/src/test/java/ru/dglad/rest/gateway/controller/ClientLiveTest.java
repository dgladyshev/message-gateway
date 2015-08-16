package ru.dglad.rest.gateway.controller;

import org.junit.Ignore;
import org.junit.Test;
import ru.dglad.rest.api.requests.SendRequest;
import ru.dglad.rest.api.responses.ErrorResponse;
import ru.dglad.rest.api.responses.Response;
import ru.dglad.rest.api.responses.SuccessSendingResponse;
import ru.dglad.rest.client.MessageClient;

public class ClientLiveTest {

	@Test
	@Ignore("manual test") //real message sending test (just change this url to any production server ^_^ )
	public void testSendSuccess() throws Exception {
		MessageClient client = new MessageClient("http://localhost:8080", "some_usr_name","some_pwd");
		SendRequest request = new SendRequest()
				.address("101") //don't forget to change the address
				.sender("Alex")
				.requestId("100500")
				.text("Text\nMessage");
		Response response = client.send(request);
		if (response instanceof SuccessSendingResponse) {
			System.out.println(((SuccessSendingResponse) response).getMessageId());
		}
		if (response instanceof ErrorResponse) {
			ErrorResponse.ErrorDetails error = ((ErrorResponse) response).getError();
			System.out.print("code: " + error.getCode() + " message: " + error.getMessage());
		}
	}

}
