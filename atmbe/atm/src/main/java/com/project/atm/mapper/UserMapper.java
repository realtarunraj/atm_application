package com.project.atm.mapper;

import org.mapstruct.Mapper;

import com.project.atm.dto.UserDTO;
import com.project.atm.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
	UserDTO userToUserDTO(User user);

	User userDTOToUser(UserDTO userDTO);

//	@Mapping(target = "id", ignore = true)
//	UserDTO createUserDTOWithoutId(User user);

//	@Mapping(target = "id", ignore = true)
	User createUser(UserDTO userdto);
}
