package ru.dglad.rest.gateway.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.dglad.rest.api.requests.SendRequest;
import ru.dglad.rest.api.responses.ErrorCode;
import ru.dglad.rest.api.responses.ErrorResponse;
import ru.dglad.rest.api.responses.Response;
import ru.dglad.rest.api.responses.SuccessSendingResponse;
import ru.dglad.rest.gateway.exceptions.MessageServiceException;
import ru.dglad.rest.gateway.service.MessageService;

import javax.validation.Valid;

@RestController
public class SendController {

	private static final Logger log = LoggerFactory.getLogger(SendController.class);

	private final MessageService messageService;

	@Autowired
	public SendController(final MessageService messageService) {
		this.messageService = messageService;
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(new SendRequest.Validator());
	}

	@RequestMapping(value = "/message", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<? extends Response> send(@Valid @RequestBody SendRequest request, BindingResult result) {
		if (result.hasErrors()) {
			log.warn("Binding result has errors: {}", result.toString());
			return createErrorResponseEntity(ErrorCode.REQUEST_CORRUPTED, true, result.toString());
		}
		try {
			String messageId = messageService.send(request);
			return new ResponseEntity<>(new SuccessSendingResponse(messageId), HttpStatus.OK);
		} catch (MessageServiceException e) {
			log.warn("Message wasn't sent", e);
			return messageServiceExceptionToResponseEntity(e);
		}
	}

	@RequestMapping(value = "/message/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<? extends Response> getDeliveryStatus(@PathVariable String id) {
		try {
			return new ResponseEntity<>(messageService.getDeliveryStatus(id), HttpStatus.OK);
		} catch (MessageServiceException e) {
			log.warn("Failed to get message status for id: {}", id, e);
			return messageServiceExceptionToResponseEntity(e);
		}
	}

	private ResponseEntity<ErrorResponse> createErrorResponseEntity(int errorCode, boolean fatal, String message) {
		return new ResponseEntity<>(new ErrorResponse(errorCode, fatal, message), mapErrorCodeToHttpStatus(errorCode));
	}

	private ResponseEntity<ErrorResponse> messageServiceExceptionToResponseEntity(MessageServiceException e) {
		return createErrorResponseEntity(e.getErrorCode(), e.isFatal(), e.getMessage());
	}

	private HttpStatus mapErrorCodeToHttpStatus(int errorCode) {
		switch (errorCode) {
			case ErrorCode.OK:
				return HttpStatus.OK;
			case ErrorCode.REQUEST_CORRUPTED:
				return HttpStatus.BAD_REQUEST;
			default:
				return HttpStatus.INTERNAL_SERVER_ERROR;
		}
	}
}
