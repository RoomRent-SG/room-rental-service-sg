package com.thiha.roomrent.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.thiha.roomrent.model.Agent;



public interface AgentRepository extends JpaRepository<Agent,Long>{
    Optional<Agent> findByUsername(String username);

    @Query("SELECT agent FROM Agent agent WHERE agent.email= :email")
    Optional<Agent> findByEmail(String email);
}
