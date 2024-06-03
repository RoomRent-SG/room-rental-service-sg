package com.thiha.roomrent.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thiha.roomrent.model.JwtToken;

public interface JwtTokenRepository extends JpaRepository<JwtToken, Long>{
    
    @Query("""
            SELECT t 
            FROM JwtToken t
            WHERE t.user.id = :id
            """)
    List<JwtToken> getTokensByUserId(@Param("id")Long id);
}
