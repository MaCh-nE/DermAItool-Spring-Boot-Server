package com.dermAItool.DermAItool.Controller;

import com.dermAItool.DermAItool.DTO.UserDTO;
import com.dermAItool.DermAItool.Services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/API/Users")       // Base URL for the controller ;
@AllArgsConstructor                 // UserService dependency injection ;
public class UserController {

    // @Autowire
    private UserService userService;

                                    // POST requests : createUser extracts the user info from the incoming JSON (@RequestBody), creates
    @PostMapping                    //                 and saves user, sends back response ;
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO savedUser = userService.createUser(userDTO);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

                                    // GET requests  : getUser fetches the user from the DB with the given id in
    @GetMapping("{id}")             //                 URL and returns the given DTO (or exception) via its response's JSON body ;
    public ResponseEntity<UserDTO> getUser(@PathVariable("id") Long id) {
        UserDTO fetchedUser = userService.getUserById(id);
        return ResponseEntity.ok(fetchedUser);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> fetchedUsers = userService.getAllUsers();
        return ResponseEntity.ok(fetchedUsers);
    }

                                    // PUT requests  : updateUser fetches the user from the DB with the given id in
    @PutMapping("{id}")             //                 URL, updates it with DTO in JSON body, saves it and returns the given DTO ;
    public ResponseEntity<UserDTO> updateUser(@PathVariable("id") Long userId, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(userId, userDTO);
        return ResponseEntity.ok(updatedUser);
    }


    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("User with Id: " + userId + " deleted successfully.");
    }

    // User Authentication simple pw check :
    @PostMapping("authenticate")
    public ResponseEntity<String> checkForUserPass(@RequestBody Map<String, String> user) {
        String mail = user.get("mail");
        String pass = user.get("password");
        if (userService.checkForUserLogin(mail, pass)) {
            return ResponseEntity.ok("Successful login.");
        }
        else {
            return ResponseEntity.ok("Login failed, wrong password");
        }
    }

    // User Data fetch via mail ;
    @GetMapping("mail/{mail}")
    public ResponseEntity<UserDTO> getUserByMail(@PathVariable String mail) {
        UserDTO user = userService.getUserByMail(mail);
        return ResponseEntity.ok(user);
    }
}