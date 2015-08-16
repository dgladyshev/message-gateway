package ru.dglad.rest.api.responses;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="response")
@XmlAccessorType(XmlAccessType.FIELD)
public class StatusResponse extends Response {
	@XmlElement
	private final long id;
	@XmlElement
	private final int status;

	public StatusResponse(final long id, final int status) {
		this.id = id;
		this.status = status;
	}

	private StatusResponse() {
		this.id = 0;
		this.status = 0;
	}

	public long getId() {
		return id;
	}

	public int getStatus() {
		return status;
	}

}
