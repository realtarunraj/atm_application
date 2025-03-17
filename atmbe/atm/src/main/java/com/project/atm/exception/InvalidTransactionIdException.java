package com.project.atm.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InvalidTransactionIdException extends RuntimeException {
	private String message;
}
