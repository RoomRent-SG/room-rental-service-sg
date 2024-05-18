package com.thiha.roomrent.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thiha.roomrent.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUsername(String username);
}
