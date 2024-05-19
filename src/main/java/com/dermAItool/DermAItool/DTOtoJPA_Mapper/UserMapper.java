package com.dermAItool.DermAItool.DTOtoJPA_Mapper;

import com.dermAItool.DermAItool.DTO.UserDTO;
import com.dermAItool.DermAItool.JPA_Entities.User;


public class UserMapper {
    public static User toUser(UserDTO userDTO) {
        return new User(
                userDTO.getId(),
                userDTO.getMail(),
                userDTO.getFirstName(),
                userDTO.getLastName(),
                userDTO.getPassword()
        );
    }

    public static UserDTO toUserDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getMail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPassword()
        );
    }
}
