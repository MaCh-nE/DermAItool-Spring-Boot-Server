package com.dermAItool.DermAItool.Repository;

import com.dermAItool.DermAItool.JPA_Entities.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface ImageRepo extends JpaRepository<Images, Long> {

    // JPQL to select all Images with user equals the first parameter (?1)
    @Query("SELECT i FROM Images i WHERE i.userId.id = ?1")
    List<Images> findBySecondaryKey(Long userId);
}
