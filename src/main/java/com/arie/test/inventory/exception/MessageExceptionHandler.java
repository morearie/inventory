package com.arie.test.inventory.exception;

import java.nio.file.AccessDeniedException;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.naming.AuthenticationException;
import javax.persistence.EntityNotFoundException;
import javax.security.auth.login.LoginException;

import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import com.arie.test.inventory.dto.GeneralWrapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handler for all exceptions and generate response in json format.
 */
@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class MessageExceptionHandler extends ResponseEntityExceptionHandler {

	private static final String UNEXPECTED_ERROR = "Unexpected error";
	private final MessageSource messageSource;

	/**
	 * Handle general error message.
	 *
	 * @param ex exception to be handled
	 * @return response entity with error message
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<GeneralWrapper<Object>> general(Exception ex) {
		printError(ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new GeneralWrapper<>().fail(HttpStatus.INTERNAL_SERVER_ERROR, UNEXPECTED_ERROR));
	}

	/**
	 * Handle badrequest error message.
	 *
	 * @param ex exception to be handled
	 * @return response entity with error message
	 */
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<GeneralWrapper<Object>> badRequest(BadRequestException ex, Locale locale) {
		printError(ex);
		String errorMessage = messageSource.getMessage(ex.getMessage(), null, locale);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new GeneralWrapper<>().fail(HttpStatus.BAD_REQUEST, errorMessage));
	}

	/**
	 * Handle notfound error message.
	 *
	 * @param ex exception to be handled
	 * @return response entity with error message
	 */
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<GeneralWrapper<Object>> notFound(EntityNotFoundException ex, Locale locale) {
		printError(ex);
		String errorMessage = messageSource.getMessage(ex.getMessage(), null, locale);
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new GeneralWrapper<>().fail(HttpStatus.NOT_FOUND, errorMessage));
	}

	/**
	 * Handle unauthorized error message.
	 *
	 * @param ex exception to be handled
	 * @return response entity with error message
	 */
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<GeneralWrapper<Object>> unauthorized(AuthenticationException ex) {
		printError(ex);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(new GeneralWrapper<>().fail(HttpStatus.UNAUTHORIZED, ex.getLocalizedMessage()));
	}
	
	/**
	 * Handle unauthorized error message.
	 *
	 * @param ex exception to be handled
	 * @return response entity with error message
	 */
	@ExceptionHandler(LoginException.class)
	public ResponseEntity<GeneralWrapper<Object>> loginfailed(LoginException ex) {
		printError(ex);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(new GeneralWrapper<>().fail(HttpStatus.UNAUTHORIZED, ex.getLocalizedMessage()));
	}

	/**
	 * Handle forbidden error message.
	 *
	 * @param ex exception to be handled
	 * @return response entity with error message
	 */
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<GeneralWrapper<Object>> forbidden(AccessDeniedException ex) {
		printError(ex);
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(new GeneralWrapper<>().fail(HttpStatus.FORBIDDEN, ex.getLocalizedMessage()));
	}

	/**
	 * Handle validation message.
	 *
	 * @param ex exception to be handled
	 * @return response entity with error message
	 */
	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<GeneralWrapper<Object>> validation(ValidationException ex) {
		printError(ex);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new GeneralWrapper<>().fail(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage()));
	}
	
	 
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<GeneralWrapper<Object>> duplicate(DataIntegrityViolationException ex) {
		printError(ex);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new GeneralWrapper<>().fail(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage()));
	}

	/**
	 * A single place to customize the response body of all validation types.
	 * <p>
	 * The default implementation sets the
	 * {@link WebUtils#ERROR_EXCEPTION_ATTRIBUTE} request attribute and creates a
	 * {@link ResponseEntity} from the given body, headers, and status.
	 *
	 * @param ex      the MethodArgumentNotValidException
	 * @param body    the body for the response
	 * @param headers the headers for the response
	 * @param status  the response status
	 * @param request the current request
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		// Get all errors
		String errors = ex.getBindingResult().getFieldErrors().stream().map(
				e -> e.getField() + ": " + messageSource.getMessage(e.getDefaultMessage(), null, request.getLocale()))
				.collect(Collectors.joining("; "));

		printError(ex);
		return ResponseEntity.status(status).body(new GeneralWrapper<>().fail(HttpStatus.BAD_REQUEST, errors));

	}

	/**
	 * A single place to customize the response body of all exception types.
	 * <p>
	 * The default implementation sets the
	 * {@link WebUtils#ERROR_EXCEPTION_ATTRIBUTE} request attribute and creates a
	 * {@link ResponseEntity} from the given body, headers, and status.
	 *
	 * @param ex      the exception
	 * @param body    the body for the response
	 * @param headers the headers for the response
	 * @param status  the response status
	 * @param request the current request
	 */
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		printError(ex);
		super.handleExceptionInternal(ex, body, headers, status, request);
		String errorMessage = messageSource.getMessage(ex.getMessage(), null, request.getLocale());
		return ResponseEntity.status(status).headers(headers)
				.body(new GeneralWrapper<>().fail(status, errorMessage));
	}

	private void printError(Exception ex) {
		logger.error("Error::{}",ex);
	}
	
}
