package com.project.atm.dto;

import javax.validation.constraints.NotNull;

import com.project.atm.enums.UserType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {

	@NotNull(message = "UserId can't be Null.")
	private Integer userId;

	@NotNull(message = "Password can't be Null.")
	private String password;
	
	@NotNull
	private UserType userType;
}
