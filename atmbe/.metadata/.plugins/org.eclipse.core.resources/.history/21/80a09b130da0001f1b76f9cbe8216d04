package com.project.atm.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import com.project.atm.enums.UserType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user_details")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userIdGenerator")
	@SequenceGenerator(name = "userIdGenerator", initialValue = 100000, allocationSize = 1)
	private Integer userId;
	
	// Account Number ...
	
	private String username;
	private String password;
	private Double balance;

	@Enumerated(EnumType.STRING)
	private UserType userType;
}
