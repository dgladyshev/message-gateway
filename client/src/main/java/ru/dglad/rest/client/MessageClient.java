package ru.dglad.rest.client;

import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.dglad.rest.api.requests.SendRequest;
import ru.dglad.rest.api.responses.Response;
import ru.dglad.rest.client.exceptions.MessageCommunicationException;
import ru.dglad.rest.client.util.MessageSerializer;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.util.Arrays;

@PropertySource("classpath:application.properties")
public class MessageClient {

	private final String url;
	private final HttpHeaders headers;

	public MessageClient(String url, String username, String password) {
		this.url = url;
		this.headers = makeHeaders(username, password);
	}

	private HttpHeaders makeHeaders(String username, String password) {
		HttpHeaders tempHeaders = new HttpHeaders();
		tempHeaders.setContentType(MediaType.APPLICATION_JSON);
		tempHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		//Basic Authorization header
		String credentials = username + ":" + password;
		tempHeaders.add("Authorization", "Basic " + new String(Base64.encodeBase64(credentials.getBytes())));
		return HttpHeaders.readOnlyHttpHeaders(tempHeaders);
	}

	public Response send(SendRequest request) throws MessageCommunicationException {
		try {
			HttpEntity<String> httpEntity = new HttpEntity<>(MessageSerializer.serialize(request), headers);
			ResponseEntity<String> response = new RestTemplate().postForEntity(url + "/message", httpEntity, String.class);
			if (response != null) {
				return MessageSerializer.parseToResponse(response.getBody());
			}
			throw new MessageCommunicationException("No response from server");
		}
		catch (IOException ex) {
			throw new MessageCommunicationException("Can't parse response", ex);
		} catch (RestClientException ex) {
			throw new MessageCommunicationException("Runtime Spring exception", ex);
		}
	}

	public Response getStatus(String msgId) throws MessageCommunicationException {
		try {
			String result = new RestTemplate().getForObject(url + "/message/{id}", String.class, msgId);
			if (result != null) {
				return MessageSerializer.parseToResponse(result);
			}
			throw new MessageCommunicationException("No response from server");
		}
		catch (IOException ex) {
			throw new MessageCommunicationException("Can't parse response", ex);
		} catch (RestClientException ex) {
			throw new MessageCommunicationException("Runtime Spring exception", ex);
		}
	}

}