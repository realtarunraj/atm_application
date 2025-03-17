package com.project.atm.service;

import java.util.List;

import com.project.atm.dto.LoginDTO;
import com.project.atm.dto.PaymentDTO;
import com.project.atm.dto.TransferDTO;
import com.project.atm.dto.UserDTO;
import com.project.atm.dto.UserResponseDTO;
import com.project.atm.entity.User;

public interface UserService {

	public User addUser(User user);

	public User updateUser(UserDTO user);

	public UserDTO getUser(Integer userId);

	public List<User> getUsers();

	public String deleteUser(Integer userId);

	public String withdrawl(PaymentDTO paymentdto);

	public String deposit(PaymentDTO paymentdto);

	public String transfer(TransferDTO transferDTO);

	public Double getBalance(Integer id);

	public UserResponseDTO userLogin(LoginDTO logindto);
}
