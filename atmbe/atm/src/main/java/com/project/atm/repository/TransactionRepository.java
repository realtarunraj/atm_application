package com.project.atm.repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.project.atm.entity.Transactions;

@Repository
@Transactional
public interface TransactionRepository extends JpaRepository<Transactions, Integer> {

	// 1. Fetch all transactions for a specific date
	@Query("SELECT t FROM Transactions t WHERE DATE(t.date) = DATE(:specificDate)")
	List<Transactions> findAllByDate(@Param("specificDate") LocalDateTime specificDate);

	// 2. Fetch transactions between two dates
	@Query("SELECT t FROM Transactions t WHERE t.date BETWEEN :startDate AND :endDate")
	List<Transactions> findAllBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
	
	// 3. Fetch all transactions of specific user
	List<Transactions> findByUserId(Integer userId);
}
