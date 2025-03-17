package com.project.atm.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.atm.enums.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Transactions {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer transactionId;

	private Integer userId;

	@Enumerated(EnumType.STRING)
	private TransactionType transactionType;
	
	private Integer senderId;
	private Integer receiverId;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime date;
	private Double TransactionAmount;
	private Double remainigBalance;
}

// from date
// b/w date
// id 
// getAll
// by user