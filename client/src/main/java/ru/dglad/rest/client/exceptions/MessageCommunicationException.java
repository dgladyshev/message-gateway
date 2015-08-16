package ru.dglad.rest.client.exceptions;

public class MessageCommunicationException extends Exception {

	public MessageCommunicationException(final String message, final Exception exc) {
		super(message, exc);
	}

	public MessageCommunicationException(final String message) {
		super(message);
	}

}
