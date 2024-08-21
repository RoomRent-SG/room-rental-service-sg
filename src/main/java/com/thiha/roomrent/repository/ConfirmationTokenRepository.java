package com.thiha.roomrent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.thiha.roomrent.model.ConfirmationToken;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long>{
    Optional<ConfirmationToken> findByTokenValue(UUID token);
}
