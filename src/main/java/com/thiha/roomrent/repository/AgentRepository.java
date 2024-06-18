package com.thiha.roomrent.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.thiha.roomrent.model.Agent;


@Repository
public interface AgentRepository extends JpaRepository<Agent,Long>{
    Optional<Agent> findByUsername(String username);

    @Query("SELECT agent FROM Agent agent WHERE agent.email= :email")
    Optional<Agent> findByEmail(String email);
}
