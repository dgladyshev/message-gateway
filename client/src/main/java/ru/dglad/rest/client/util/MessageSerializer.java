package ru.dglad.rest.client.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.dglad.rest.api.responses.ErrorResponse;
import ru.dglad.rest.api.responses.Response;
import ru.dglad.rest.api.responses.StatusResponse;
import ru.dglad.rest.api.responses.SuccessSendingResponse;
import ru.dglad.rest.client.exceptions.MessageCommunicationException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class MessageSerializer {

	private static final ObjectMapper mapper = new ObjectMapper();

	public static String serialize(Object pojo) throws IOException {
		Writer writer = new StringWriter();
		mapper.writeValue(writer, pojo);
		return writer.toString();
	}

	public static <T> T deserialize(java.lang.String json, Class<T> type) throws IOException {
		return mapper.readValue(json, type);
	}

	public static Response parseToResponse(String result)
			throws IOException, MessageCommunicationException {
		final Response response;
		if (result.toLowerCase().contains("messageid")) {
			response = deserialize(result, SuccessSendingResponse.class);
		} else if (result.toLowerCase().contains("status")) {
			response = deserialize(result, StatusResponse.class);
		} else if (result.toLowerCase().contains("fatal")) {
			response = deserialize(result, ErrorResponse.class);
		} else {
			throw new MessageCommunicationException("Unknown JSON returned");
		}
		return response;
	}

}
