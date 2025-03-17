package com.project.atm.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.project.atm.enums.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {

	@NotNull(message = "User ID cannot be null")
	private Integer userId;

	@NotNull(message = "Amount cannot be null")
	@Min(value = 1, message = "Amount must be greater than or equal to 1")
	private Double amount;

	private TransactionType transactionType;
}
