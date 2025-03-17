package com.project.atm.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferDTO {

	@NotNull(message = "senderId cannot be null")
	private Integer senderId;

	@NotNull(message = "receiverId cannot be null")
	private Integer receiverId;

	@NotNull(message = "amount cannot be null")
	@Positive(message = "amount must be a positive number")
	private Double amount;
}
