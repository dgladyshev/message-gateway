package ru.dglad.rest.gateway.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.dglad.rest.api.requests.SendRequest;
import ru.dglad.rest.gateway.dao.MessageRepository;
import ru.dglad.rest.gateway.exceptions.MessageServiceException;
import ru.dglad.rest.api.responses.StatusResponse;

@Service
@Profile("production")
public class MessageService {

	private static final Logger log = LoggerFactory.getLogger(MessageService.class);
	private final MessageRepository messageRepository;

	@Autowired
	public MessageService(MessageRepository messageRepository) {
		this.messageRepository = messageRepository;
	}

	/**
	 * Sends a message to a specific address (channel depends of message type)
	 * Important: message will be inserted in some DB queue, not sended,
	 * because it's just a rest gateway application aimed to collect messages from clients
	 * @param sendRequest message details
	 * @return message id of corresponding queue item
	 * @throws MessageServiceException if something went wrong
	 */
	public String send(SendRequest sendRequest) throws MessageServiceException {
		try {
			String sendId = messageRepository.getMessageIdBySender(sendRequest.getSender(),
					sendRequest.getRequestId());
			if (sendId != null) {
				return sendId;
			}
			long messageId = messageRepository.insert(sendRequest);
			return String.valueOf(messageId);
		} catch (DataAccessException e) {
			log.error("Database exception", e);
			throw new MessageServiceException(3, false, "Internal server error");
		}
	}

	/**
	 * Gets delivery status of previously sended message
	 * @param messageId message details
	 * @return StatusResponse object with message status specifics
	 * @throws MessageServiceException if something went wrong
	 */
	public StatusResponse getDeliveryStatus(String messageId) throws MessageServiceException {
		try {
			return messageRepository.getMessageStatus(Long.valueOf(messageId));
		} catch (EmptyResultDataAccessException e) {
			throw new MessageServiceException(4, true, String.format("Message with id %s is not found", messageId));
		} catch (DataAccessException e) {
			log.error("Database exception", e);
			throw new MessageServiceException(3, false, "Internal server error");
		}
	}
}
