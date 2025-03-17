package com.project.atm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.project.atm.entity.User;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {

	// 1. Fetch the balance of user
	@Query("SELECT u.balance FROM user_details u WHERE u.userId = :id")
	Double findUserBalanceById(@Param("id") Integer id);
}