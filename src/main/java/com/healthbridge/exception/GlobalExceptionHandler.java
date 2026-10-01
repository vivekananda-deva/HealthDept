package com.healthbridge.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// Thrown by our own service layer when a Hospital/Doctor/Patient/Appointment
	// id doesn't exist in the DB.
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex) {

		Map<String, Object> error = new LinkedHashMap<>();
		error.put("timestamp", LocalDateTime.now());
		error.put("status", HttpStatus.NOT_FOUND.value());
		error.put("error", "NOT_FOUND");
		error.put("message", ex.getMessage());

		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	// Thrown automatically by Spring when @Valid fails on a @RequestBody DTO
	// (e.g. a @NotBlank field was left empty).
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {

		Map<String, String> fieldErrors = new LinkedHashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(fe ->
				fieldErrors.put(fe.getField(), fe.getDefaultMessage()));

		Map<String, Object> error = new LinkedHashMap<>();
		error.put("timestamp", LocalDateTime.now());
		error.put("status", HttpStatus.BAD_REQUEST.value());
		error.put("error", "VALIDATION_FAILED");
		error.put("message", "One or more fields are invalid");
		error.put("fieldErrors", fieldErrors);

		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	// Catch-all fallback so unexpected errors still come back as clean JSON
	// instead of a raw stack trace / default Whitelabel error page.
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {

		Map<String, Object> error = new HashMap<>();
		error.put("timestamp", LocalDateTime.now());
		error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
		error.put("error", "INTERNAL_ERROR");
		error.put("message", ex.getMessage());

		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
