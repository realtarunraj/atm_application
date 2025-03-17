package com.project.atm.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.project.atm.enums.UserType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

	private Integer userId;
	private String username;
	private Double balance;

	@Enumerated(EnumType.STRING)
	private UserType userType;
}
