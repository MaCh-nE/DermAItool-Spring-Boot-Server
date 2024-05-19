package com.dermAItool.DermAItool.Services.Implementation;

import com.dermAItool.DermAItool.DTO.UserDTO;
import com.dermAItool.DermAItool.DTOtoJPA_Mapper.UserMapper;
import com.dermAItool.DermAItool.Exceptions.EntityNotFoundException;
import com.dermAItool.DermAItool.Exceptions.UserFoundException;
import com.dermAItool.DermAItool.JPA_Entities.User;
import com.dermAItool.DermAItool.Repository.UserRepo;
import com.dermAItool.DermAItool.Services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor   // userRepo is injected via the All Args Controller via Spring Boot's Dependency Injection
public class UserServiceImplementation implements UserService {

    private UserRepo userRepo;


    @Override
    public UserDTO createUser(UserDTO userDTO) {
        // Fetching user if exists already ;
        Optional<User> existingUser = userRepo.findByMail(userDTO.getMail());
        // if Found, throw found Exception ;
        existingUser.ifPresent(user -> {
            throw new UserFoundException("User with mail: " + user.getMail() + " already exists, please Log-In.");
        });
        // -----> ELSE :
        // Create userJPA Entity ;
        User user = UserMapper.toUser(userDTO);
        // Save it in DB ;
        User savedUser = userRepo.save(user);
        // Return the saved user's DTO ;
        return UserMapper.toUserDTO(savedUser);
    }

    @Override
    public UserDTO getUserById(Long id) {
        // fetch user from DB ;
        User fetchedUser = userRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("User with given ID: " + id + " not found."));
        return UserMapper.toUserDTO(fetchedUser);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        // fetch all users from DB ;
        List<User> allUsers = userRepo.findAll();
        return allUsers.stream().map(UserMapper::toUserDTO).collect(Collectors.toList());
    }

    @Override
    public UserDTO updateUser(Long userId, UserDTO updatedUser) {
        // Fetching for the User JPA ! (Don't use the getUserById, you will just update an object of the entity) ;
        User fetchedUser = userRepo.findById(userId).orElseThrow(() -> new EntityNotFoundException("User with given ID: " + userId + " not found."));
        // Updating the User JPA entity ;
        fetchedUser.setMail(updatedUser.getMail());
        fetchedUser.setFirstName(updatedUser.getFirstName());
        fetchedUser.setLastName(updatedUser.getLastName());
        // Saving the JPA entity via the repo ;
        User updated = userRepo.save(fetchedUser);
        // Returning its DTO ;
        return UserMapper.toUserDTO(updated);
    }

    @Override
    public void deleteUser(Long id) {
        // Fetching for the User JPA ! (Don't use the deleteById method to throw EntityNotFoundException if not found) ;
        User fetchedUser = userRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("User with given ID: " + id + " not found."));
        // Deleting the JPA entity ;
        userRepo.delete(fetchedUser);
        return;
    }

    @Override
    public boolean checkForUserLogin(String mail, String password) {
        User user = userRepo.findByMail(mail).orElseThrow(() -> new EntityNotFoundException("User with given mail: " + mail + " not found."));
        return user.getPassword().equals(password);
    }

    @Override
    public UserDTO getUserByMail(String mail) {
        User user = userRepo.findByMail(mail).orElseThrow(() -> new EntityNotFoundException("User with given mail: " + mail + " not found."));
        return UserMapper.toUserDTO(user);
    }

}
