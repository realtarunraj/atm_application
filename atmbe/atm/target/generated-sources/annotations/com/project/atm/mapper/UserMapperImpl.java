package com.project.atm.mapper;

import com.project.atm.dto.UserDTO;
import com.project.atm.entity.User;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-10T16:19:15+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.21.0.v20200304-1404, environment: Java 1.8.0_151 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO userToUserDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setBalance( user.getBalance() );
        userDTO.setPassword( user.getPassword() );
        userDTO.setUserId( user.getUserId() );
        userDTO.setUsername( user.getUsername() );

        return userDTO;
    }

    @Override
    public User userDTOToUser(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        User user = new User();

        user.setBalance( userDTO.getBalance() );
        user.setPassword( userDTO.getPassword() );
        user.setUserId( userDTO.getUserId() );
        user.setUsername( userDTO.getUsername() );

        return user;
    }

    @Override
    public User createUser(UserDTO userdto) {
        if ( userdto == null ) {
            return null;
        }

        User user = new User();

        user.setBalance( userdto.getBalance() );
        user.setPassword( userdto.getPassword() );
        user.setUserId( userdto.getUserId() );
        user.setUsername( userdto.getUsername() );

        return user;
    }
}
