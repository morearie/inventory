package com.arie.test.inventory.exception;

/**
 * {@link Exception} that will be thrown when validation fails.
 */
public class ValidationException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Constructor for {@link ValidationException}.
     *
     * @param message message of the exception
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Constructor for {@link ValidationException}.
     *
     * @param message message of the exception
     * @param cause   the cause exception
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}