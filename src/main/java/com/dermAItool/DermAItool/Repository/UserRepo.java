package com.dermAItool.DermAItool.Repository;

import com.dermAItool.DermAItool.JPA_Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;

//   @Repository annotation for SpringBoot is already inducted in an inherited class via JpaRepository ;
//   A repo. interacts with JPA entities only ;
public interface UserRepo extends JpaRepository<User, Long> {

    // Find user by email :
    @Query("SELECT i FROM User i WHERE i.mail = ?1")
    Optional<User> findByMail(String mail);
}