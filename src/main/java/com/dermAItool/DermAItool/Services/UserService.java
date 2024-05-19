package com.dermAItool.DermAItool.Services;

import com.dermAItool.DermAItool.DTO.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    UserDTO getUserById(Long id);
    List<UserDTO> getAllUsers();
    UserDTO updateUser(Long userId, UserDTO updatedUser);
    void deleteUser(Long id);
    boolean checkForUserLogin(String mail, String password);
    UserDTO getUserByMail(String mail);
}
