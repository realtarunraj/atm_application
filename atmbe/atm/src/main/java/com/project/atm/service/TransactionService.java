package com.project.atm.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.project.atm.entity.Transactions;

public interface TransactionService {

	public void addTransaction(Transactions transaction);

	public List<Transactions> getTransactions();

	public Transactions getTransactionById(Integer transactionId);

	public List<Transactions> getTransactionByUserId(Integer userId);

	public List<Transactions> getTransactionByDate(LocalDateTime date);

	public List<Transactions> getTransactionBtwDate(LocalDateTime from, LocalDateTime to);

}
