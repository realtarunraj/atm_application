package com.project.atm.serviceImpl;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.atm.entity.Transactions;
import com.project.atm.exception.InvalidTransactionIdException;
import com.project.atm.repository.TransactionRepository;
import com.project.atm.service.TransactionService;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private TransactionRepository transactionRepo;

	@Override
	public void addTransaction(Transactions transaction) {
		transactionRepo.save(transaction);
	}

	@Override
	public List<Transactions> getTransactions() {
		return transactionRepo.findAll();
	}

	@Override
	public Transactions getTransactionById(Integer transactionId) {
		Optional<Transactions> transaction = transactionRepo.findById(transactionId);
		if (transaction.isEmpty())
			throw new InvalidTransactionIdException();

		return transaction.get();
	}

	@Override
	public List<Transactions> getTransactionByUserId(Integer userId) {
		return transactionRepo.findByUserId(userId);
	}

	@Override
	public List<Transactions> getTransactionByDate(LocalDateTime date) {
		return transactionRepo.findAllByDate(date);
	}

	@Override
	public List<Transactions> getTransactionBtwDate(LocalDateTime from, LocalDateTime to) {
		
		return transactionRepo.findAllBetweenDates(from,to);
	}
}
