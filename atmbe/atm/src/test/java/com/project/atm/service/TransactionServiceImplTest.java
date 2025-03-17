package com.project.atm.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.project.atm.entity.Transactions;
import com.project.atm.exception.InvalidTransactionIdException;
import com.project.atm.repository.TransactionRepository;
import com.project.atm.serviceImpl.TransactionServiceImpl;

class TransactionServiceImplTest {

	@InjectMocks
	private TransactionServiceImpl transactionService;

	@Mock
	private TransactionRepository transactionRepo;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testAddTransaction() {
		Transactions transaction = new Transactions();
		transaction.setTransactionId(1);

		transactionService.addTransaction(transaction);
		verify(transactionRepo, times(1)).save(transaction);
	}

	@Test
	void testGetTransactions() {
		List<Transactions> transactions = Arrays.asList(new Transactions(), new Transactions());
		when(transactionRepo.findAll()).thenReturn(transactions);

		List<Transactions> result = transactionService.getTransactions();
		assertEquals(2, result.size());
		verify(transactionRepo, times(1)).findAll();
	}

	@Test
	void testGetTransactionById() {
		Transactions transaction = new Transactions();
		transaction.setTransactionId(1);
		when(transactionRepo.findById(1)).thenReturn(Optional.of(transaction));

		Transactions result = transactionService.getTransactionById(1);
		assertNotNull(result);
		assertEquals(1, result.getTransactionId());
	}

	@Test
    void testGetTransactionById_InvalidId() {
        when(transactionRepo.findById(99)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            transactionService.getTransactionById(99);
        });

        assertEquals(InvalidTransactionIdException.class, exception.getClass());
    }

	@Test
	void testGetTransactionByUserId() {
		List<Transactions> transactions = Arrays.asList(new Transactions(), new Transactions());
		when(transactionRepo.findByUserId(1)).thenReturn(transactions);

		List<Transactions> result = transactionService.getTransactionByUserId(1);
		assertEquals(2, result.size());
		verify(transactionRepo, times(1)).findByUserId(1);
	}

	@Test
	void testGetTransactionByDate() {
		LocalDateTime specificDate = LocalDateTime.now();
		List<Transactions> transactions = Arrays.asList(new Transactions());
		when(transactionRepo.findAllByDate(specificDate)).thenReturn(transactions);

		List<Transactions> result = transactionService.getTransactionByDate(specificDate);
		assertEquals(1, result.size());
		verify(transactionRepo, times(1)).findAllByDate(specificDate);
	}

	@Test
	void testGetTransactionBtwDate() {
		LocalDateTime from = LocalDateTime.now();
		LocalDateTime to = LocalDateTime.now();
		List<Transactions> transactions = Arrays.asList(new Transactions(), new Transactions());
		when(transactionRepo.findAllBetweenDates(from, to)).thenReturn(transactions);

		List<Transactions> result = transactionService.getTransactionBtwDate(from, to);
		assertEquals(2, result.size());
		verify(transactionRepo, times(1)).findAllBetweenDates(from, to);
	}
}
