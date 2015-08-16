package ru.dglad.rest.api.responses;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="response")
@XmlAccessorType(XmlAccessType.FIELD)
public class ErrorResponse extends Response {

	@XmlElement
	private final ErrorDetails error;

	public ErrorResponse(final int code, final boolean fatal, final String message) {
		this.error = new ErrorDetails(code, fatal, message);
	}

	private ErrorResponse() {
		this.error = null;
	}

	public ErrorDetails getError() {
		return error;
	}

	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class ErrorDetails {
		@XmlElement
		private final int code;
		@XmlElement
		private final boolean fatal;
		@XmlElement
		private final String message;

		private ErrorDetails(final int code, final boolean fatal, final String message) {
			this.code = code;
			this.fatal = fatal;
			this.message = message;
		}

		private ErrorDetails() {
			this.code = 0;
			this.fatal = false;
			this.message = null;
		}

		public int getCode() {
			return code;
		}

		public boolean isFatal() {
			return fatal;
		}

		public String getMessage() {
			return message;
		}
	}
}
