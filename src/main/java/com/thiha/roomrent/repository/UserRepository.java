package com.thiha.roomrent.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.thiha.roomrent.model.UserModel;

@Repository
public interface  UserRepository extends JpaRepository<UserModel, Long>{
    
    Optional<UserModel> findByUsername(String username);
}
