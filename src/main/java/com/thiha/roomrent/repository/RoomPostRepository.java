package com.thiha.roomrent.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.thiha.roomrent.model.RoomPost;

public interface RoomPostRepository extends JpaRepository<RoomPost, Long>{

    @Query("SELECT * FROM RoomPost roomPost WHERE roomPost.agent_id =:agentId")
   List<RoomPost> getRoomPostsByAgentId(Long agentId);
}
