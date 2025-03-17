package com.project.atm.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	@NotNull
	private Integer userId;

	private String username;
	private String password;
	private Double balance;
}