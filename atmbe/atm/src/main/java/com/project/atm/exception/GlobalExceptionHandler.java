package com.project.atm.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleAllException(Exception ex) {
		if (ex.getClass() == InsufficientBalanceException.class) {
			return new ResponseEntity<String>("Insufficient Balance", HttpStatus.BAD_REQUEST);
		} else if (ex.getClass() == InvalidTransactionIdException.class) {
			return new ResponseEntity<String>("Invalid Transaction Id", HttpStatus.BAD_REQUEST);
		} else if (ex.getClass() == ResponseStatusException.class) {
			ResponseStatusException re = (ResponseStatusException) ex;
			return new ResponseEntity<String>(re.getMessage(), re.getStatus());
		} else {
			return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}
