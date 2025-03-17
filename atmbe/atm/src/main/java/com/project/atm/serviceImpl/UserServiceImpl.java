package com.project.atm.serviceImpl;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hibernate.type.descriptor.java.LocalDateTimeJavaDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.project.atm.dto.LoginDTO;
import com.project.atm.dto.PaymentDTO;
import com.project.atm.dto.TransferDTO;
import com.project.atm.dto.UserDTO;
import com.project.atm.dto.UserResponseDTO;
import com.project.atm.entity.Transactions;
import com.project.atm.entity.User;
import com.project.atm.enums.TransactionType;
import com.project.atm.exception.InsufficientBalanceException;
import com.project.atm.mapper.UserMapper;
import com.project.atm.repository.UserRepository;
import com.project.atm.service.TransactionService;
import com.project.atm.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserMapper userMapper;

	public User addUser(User user) {
		// Encode the Password ...
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		User userCreated = userRepo.save(user);
		return userCreated;
	}

	public User updateUser(UserDTO user) {
		if (user.getUserId() == null)
			throw new RuntimeException("User Id is invalid");

		Optional<User> existingUser = userRepo.findById(user.getUserId());
		if (existingUser.isEmpty())
			throw new RuntimeException("User not Found");
		if (user.getBalance() != null)
			existingUser.get().setBalance(user.getBalance());
		if (user.getPassword() != null)
			existingUser.get().setPassword(passwordEncoder.encode(user.getPassword()));
		if (user.getUsername() != null)
			existingUser.get().setUsername(user.getUsername());

		userRepo.save(existingUser.get());

		return existingUser.get();
	}

	public UserDTO getUser(Integer userId) {
		Optional<User> user = userRepo.findById(userId);
		if (user.isEmpty())
			throw new RuntimeException("User Does not exists");
		return this.userMapper.userToUserDTO(user.get());
	}

	public List<User> getUsers() {
		return userRepo.findAll();
	}

	public String deleteUser(Integer userId) {
		if (userId == null)
			throw new RuntimeException("Invalid UserId");
		userRepo.deleteById(userId);
		return "User with UserId " + userId + " deleted Successfully.";
	}

	@Override
	public UserResponseDTO userLogin(LoginDTO logindto) {
		Optional<User> existingUser = userRepo.findById(logindto.getUserId());

		if (existingUser.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Id not registered.");
		}

		User user = existingUser.get();

		// Setting the values except Password which we want to show as user Response ..
		UserResponseDTO userResponse = new UserResponseDTO();
		userResponse.setUserId(user.getUserId());
		userResponse.setUsername(user.getUsername());
		userResponse.setBalance(user.getBalance());
		userResponse.setUserType(user.getUserType());

		if (!passwordEncoder.matches(logindto.getPassword(), user.getPassword())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Password is Incorrect");
		}

		if (!logindto.getUserType().equals(existingUser.get().getUserType())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid credentials");
		}

		return userResponse;
	}

	@Override
	public Double getBalance(Integer id) {
		Double balance = userRepo.findUserBalanceById(id);
		return balance;
	}

	public String withdrawl(PaymentDTO paymentdto) {
		if (paymentdto.getTransactionType() == null) {
			paymentdto.setTransactionType(TransactionType.Withdrawl);
		}
		Optional<User> user = userRepo.findById(paymentdto.getUserId());
		if (user.isEmpty())
			throw new RuntimeException("Invalid UserId");
		if (user.get().getBalance() < paymentdto.getAmount())
			throw new InsufficientBalanceException();
		user.get().setBalance(user.get().getBalance() - paymentdto.getAmount());

		userRepo.save(user.get());

		if (paymentdto.getTransactionType() == TransactionType.Withdrawl) {
			transactionService.addTransaction(
					createTransaction(user.get(), null, null, paymentdto.getAmount(), paymentdto.getTransactionType()));
		}
		return "Amount of " + paymentdto.getAmount() + " has been debited from your account & your current balance is "
				+ user.get().getBalance();
	}

	public String deposit(PaymentDTO paymentdto) {
		if (paymentdto.getTransactionType() == null) {
			paymentdto.setTransactionType(TransactionType.Deposit);
		}
		Optional<User> user = userRepo.findById(paymentdto.getUserId());
		if (user.isEmpty())
			throw new RuntimeException("Invalid UserId");
		user.get().setBalance(user.get().getBalance() + paymentdto.getAmount());

		userRepo.save(user.get());

		if (paymentdto.getTransactionType() == TransactionType.Deposit) {
			transactionService.addTransaction(
					createTransaction(user.get(), null, null, paymentdto.getAmount(), paymentdto.getTransactionType()));
		}
		return "Amount of " + paymentdto.getAmount() + " has been credited to your account & your current balance is "
				+ user.get().getBalance();
	}

	public String transfer(TransferDTO transferDTO) {
		if (isUser(transferDTO.getSenderId()) && isUser(transferDTO.getReceiverId())
				&& transferDTO.getSenderId() != transferDTO.getReceiverId()) {

			PaymentDTO withdrawlDto = new PaymentDTO(transferDTO.getSenderId(), transferDTO.getAmount(),
					TransactionType.Transfer);
			PaymentDTO depositDto = new PaymentDTO(transferDTO.getReceiverId(), transferDTO.getAmount(),
					TransactionType.Transfer);

			withdrawl(withdrawlDto);
			deposit(depositDto);

			transactionService.addTransaction(
					createTransaction(userRepo.findById(transferDTO.getSenderId()).get(), transferDTO.getSenderId(),
							transferDTO.getReceiverId(), transferDTO.getAmount(), TransactionType.Transfer));
			return "Amount of " + transferDTO.getAmount() + " has been transfered from "
					+ userRepo.findById(transferDTO.getSenderId()).get().getUsername() + "'s account to "
					+ userRepo.findById(transferDTO.getReceiverId()).get().getUsername() + "'s account.";
		}
		throw new RuntimeException("Invalid Sender or Receiver Id.");
	}

	private boolean isUser(Integer userId) {
		return userRepo.existsById(userId);
	}

	private Transactions createTransaction(User user, Integer senderId, Integer receiverId, Double transactionAmt,
			TransactionType transactionType) {
		Transactions transaction = new Transactions();
		transaction.setDate(LocalDateTime.now());
		transaction.setUserId(user.getUserId());
		transaction.setSenderId(senderId);
		transaction.setReceiverId(receiverId);
		transaction.setTransactionAmount(transactionAmt);
		transaction.setRemainigBalance(user.getBalance());
		transaction.setTransactionType(transactionType);

		return transaction;
	}
}
