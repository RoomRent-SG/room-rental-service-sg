package com.thiha.roomrent.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.thiha.roomrent.model.JwtToken;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtToken, Long>{
    
    @Query("""
            SELECT t 
            FROM JwtToken t
            WHERE t.user.id = :id
            """)
    List<JwtToken> getTokensByUserId(@Param("id")Long id);


    @Query("""
            SELECT token
            FROM JwtToken token
            WHERE token.token = :stringToken
            """)
    Optional<JwtToken> getTokenByStringToken(@Param("stringToken")String stringToken);
}
