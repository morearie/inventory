package com.arie.test.inventory.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * {@link Exception} that will be thrown when validation fails.
 */
@Setter
@Getter
public class BadRequestException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3176981433794809833L;

	private final String[] objectMessage;
	
	public BadRequestException(String message) {
		super(message);
		this.objectMessage = null;
	}

	/**
	 * Constructor for {@link BadRequestException}.
	 *
	 * @param message message of the exception
	 */
	public BadRequestException(String message, String[] objectMessage) {
		super(message);
		this.objectMessage = objectMessage;
	}

	/**
	 * Constructor for {@link BadRequestException}.
	 *
	 * @param message message of the exception
	 * @param cause   the cause exception
	 */
	public BadRequestException(String message, Throwable cause, String[] objectMessage) {
		super(message, cause);
		this.objectMessage = objectMessage;
	}

}