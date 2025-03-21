package com.project.atm.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.project.atm.dto.PaymentDTO;
import com.project.atm.dto.TransferDTO;
import com.project.atm.dto.UserDTO;
import com.project.atm.entity.Transactions;
import com.project.atm.entity.User;
import com.project.atm.enums.TransactionType;
import com.project.atm.repository.UserRepository;
import com.project.atm.serviceImpl.TransactionServiceImpl;
import com.project.atm.serviceImpl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private UserRepository userRepo;

	@InjectMocks
	private UserServiceImpl userService; // Should be at top...

	@Mock
	private TransactionServiceImpl transactionService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testAddUser() {
		// Creating user and setting some value
		User user = new User();
		user.setPassword("plainPassword");

		// Mocking the password encoding and user repository behavior
		when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
		when(userRepo.save(any(User.class))).thenReturn(user);

		// Calling the real addUser method form service
		User userCreated = userService.addUser(user);

		// Validating that the password was encoded
		assertEquals("encodedPassword", userCreated.getPassword());

	}

	@Test
	public void testUpdateUser() {
		// hardcoding
		UserDTO userDto = new UserDTO();
		userDto.setUserId(1);
		userDto.setUsername("newname");
		userDto.setPassword("newpassword");
		userDto.setBalance(2500.0);

		User existingUser = new User();
		existingUser.setUserId(1);
		existingUser.setUsername("originalname");

		when(userRepo.findById(1)).thenReturn(Optional.of(existingUser));
		when(passwordEncoder.encode("newpassword")).thenReturn("newEncodedPassword");

		// Action using the service
		User updatedUser = userService.updateUser(userDto);

		// check equals
		assertEquals("newEncodedPassword", updatedUser.getPassword());
		assertEquals("newname", updatedUser.getUsername());
		assertEquals(2500.0, updatedUser.getBalance());
	}

//	@Test
//	public void testGetUser() {
//		User user = new User();
//		user.setUserId(1);
//
//		when(userRepo.findById(1)).thenReturn(Optional.of(user));
//
//		UserDTO userFinded = userService.getUser(1);
//		assertEquals(user.getUsername(), userFinded.getUsername());
//	}

	@Test
	public void testGetAllUsers() {
		User user1 = new User();
		User user2 = new User();

		user1.setUsername("tarun");
		user2.setUsername("deepak");
		List<User> listOfUser = Arrays.asList(user1, user2);

		// Tell what to do when that method of repo is called ...
		when(userRepo.findAll()).thenReturn(listOfUser);

		// use the method of service ...
		List<User> userList = userService.getUsers();

		assertEquals(2, listOfUser.size());
		assertEquals("tarun", userList.get(0).getUsername());
		assertEquals("deepak", userList.get(1).getUsername());
	}

	@Test
	public void testDeleteUser() {
		User user = new User();
		user.setUserId(1);

		doNothing().when(userRepo).deleteById(1);
		String message = userService.deleteUser(1);

		assertEquals("User with UserId 1 deleted Successfully.", message);
		verify(userRepo, times(1)).deleteById(1);
	}

	@Test
	public void testWithdrawl() {
		PaymentDTO paymentdto = new PaymentDTO();
		paymentdto.setUserId(1);
		paymentdto.setAmount(900.0);
		paymentdto.setTransactionType(TransactionType.Withdrawl);

		User user = new User();
		user.setUserId(1);
		user.setBalance(5000.0);

		// Tell what will happen when the userRepo method is called.
		when(userRepo.findById(1)).thenReturn(Optional.of(user));

		// Service method
		String message = userService.withdrawl(paymentdto);
		assertEquals("Amount of 900.0 has been debited from your account & your current balance is 4100.0", message);
		verify(transactionService, times(1)).addTransaction(any(Transactions.class));
	}

	@Test
	public void testDeposit() {
		PaymentDTO paymentdto = new PaymentDTO();
		paymentdto.setUserId(1);
		paymentdto.setAmount(400.0);
		paymentdto.setTransactionType(TransactionType.Deposit);

		// Create a User ...
		User user = new User();
		user.setUserId(1);
		user.setBalance(900.0);

		// Tell what to do when userRepo method is being called ...
		when(userRepo.findById(1)).thenReturn(Optional.of(user));

		// Implement the userService Method
		String message = userService.deposit(paymentdto);

		// Check if the expected and actual result is equal or not ...
		assertEquals("Amount of 400.0 has been credited to your account & your current balance is 1300.0", message);
		verify(transactionService, times(1)).addTransaction(any(Transactions.class));
	}

	@Test
	public void testTransfer() {
		TransferDTO transferDTO = new TransferDTO();
		transferDTO.setSenderId(1);
		transferDTO.setReceiverId(5);
		transferDTO.setAmount(400.0);

		// Create two user, One as sender and other as receiver ...
		User sender = new User();
		sender.setUserId(1);
		sender.setBalance(1300.0);
		sender.setUsername("Tarun");

		User receiver = new User();
		receiver.setUserId(5);
		receiver.setBalance(2000.0);
		receiver.setUsername("Deepak");

		// Check the existence of User & receiver ...
		when(userRepo.existsById(1)).thenReturn(true);
		when(userRepo.existsById(5)).thenReturn(true);

		// tell what do when the findById is being called for user and receiver ...
		when(userRepo.findById(1)).thenReturn(Optional.of(sender));
		when(userRepo.findById(5)).thenReturn(Optional.of(receiver));

		// Implement transfer method of userService ...
		String message = userService.transfer(transferDTO);

		// Check if the expected and actual result is same ...
		assertEquals("Amount of 400.0 has been transfered from Tarun's account to Deepak's account.", message);
		verify(transactionService, times(1)).addTransaction(any(Transactions.class));
	}
}
