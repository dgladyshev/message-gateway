package ru.dglad.rest.api.responses;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="response")
@XmlAccessorType(XmlAccessType.FIELD)
public class SuccessSendingResponse extends Response {

	@XmlElement
	private final String messageId;

	public SuccessSendingResponse(final String messageId) {
		this.messageId = messageId;
	}

	private SuccessSendingResponse() {
		this.messageId = null;
	}

	public String getMessageId() {
		return messageId;
	}
}
