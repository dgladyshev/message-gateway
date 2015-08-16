package ru.dglad.rest.gateway.exceptions;

public class MessageServiceException extends Exception {

	private final int errorCode;
	private final boolean fatal;

	public MessageServiceException(final int errorCode, final boolean fatal, final String message) {
		super(message);
		this.errorCode = errorCode;
		this.fatal = fatal;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public boolean isFatal() {
		return fatal;
	}
}
