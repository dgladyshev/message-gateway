package ru.dglad.rest.api.requests;

import org.springframework.validation.Errors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="request")
@XmlAccessorType(XmlAccessType.FIELD)
public class SendRequest {

	@NotNull
	@XmlElement(required = true)
	private String address;

	@NotNull
	@XmlElement(required = true)
	private String sender;

	@NotNull
	@Pattern(regexp = "\\d+")
	@XmlElement(required = true)
	private String requestId;

	@Min(0)
	@XmlElement(required = false)
	private Integer typeId;

	@XmlElement(required = false)
	@Pattern(regexp = "(.|\\n)+")
	private String text;

	public String getAddress() {
		return address;
	}

	public SendRequest address(final String address) {
		this.address = address;
		return this;
	}

	public String getSender() {
		return sender;
	}

	public SendRequest sender(final String sender) {
		this.sender = sender;
		return this;
	}

	public String getRequestId() {
		return requestId;
	}

	public SendRequest requestId(final String requestId) {
		this.requestId = requestId;
		return this;
	}
	public Integer getTypeId() {
		return typeId;
	}

	public SendRequest typeId(Integer typeId) {
		this.typeId = typeId;
		return this;
	}

	public String getText() {
		return text;
	}

	public SendRequest text(final String text) {
		this.text = text;
		return this;
	}

	public static class Validator implements org.springframework.validation.Validator {

		@Override
		public boolean supports(final Class<?> clazz) {
			return SendRequest.class.equals(clazz);
		}

		@Override
		public void validate(final Object target, final Errors errors) {
			SendRequest request = (SendRequest)target;
			if (request.text == null) {
				errors.rejectValue("text", "text.absent");
			}
		}
	}
}
