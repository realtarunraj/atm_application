package com.project.atm.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.atm.entity.Transactions;
import com.project.atm.service.TransactionService;

@CrossOrigin
@RestController
@RequestMapping("/transaction")
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	// Get all transaction
	@GetMapping()
	public List<Transactions> getTransactions() {
		return transactionService.getTransactions();
	}

	// Get all transaction of specific userId
	@GetMapping("/user/{id}")
	public List<Transactions> getTransactionByUserId(@PathVariable Integer id) {
		return transactionService.getTransactionByUserId(id);
	}

	// Get transactions of specific transaction Id
	@GetMapping("/{id}")
	public Transactions getTransactionByTransactionId(@PathVariable Integer id) {
		return transactionService.getTransactionById(id);
	}
	
	// Getting transaction for specific date
	@GetMapping("/ofdate")
	public List<Transactions> getTransactionOfDate(
			@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
		return transactionService.getTransactionByDate(date);
	}

	// Getting transaction between two dates
	@GetMapping("/btwdates")
	public List<Transactions> getTransactionBTWDates(
			@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
			@RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
		return transactionService.getTransactionBtwDate(from, to);
	}
}
