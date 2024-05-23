package com.thiha.roomrent.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thiha.roomrent.model.RoomPost;

public interface RoomPostRepository extends JpaRepository<RoomPost, Long>{

    @Query("SELECT roomPost FROM RoomPost roomPost WHERE roomPost.agent.id = :agentId")
   List<RoomPost> getRoomPostsByAgentId(@Param("agentId")Long agentId);
}
