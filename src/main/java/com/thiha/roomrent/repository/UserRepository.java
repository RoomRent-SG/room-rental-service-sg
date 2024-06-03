package com.thiha.roomrent.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thiha.roomrent.model.UserModel;

public interface  UserRepository extends JpaRepository<UserModel, Long>{
    
    Optional<UserModel> findByUsername(String username);
}
