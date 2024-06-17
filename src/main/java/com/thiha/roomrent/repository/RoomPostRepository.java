package com.thiha.roomrent.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.thiha.roomrent.model.RoomPost;

@Repository
public interface RoomPostRepository extends JpaRepository<RoomPost, Long>, JpaSpecificationExecutor<RoomPost>{

    @Query("SELECT roomPost FROM RoomPost roomPost WHERE roomPost.agent.id = :agentId")
   List<RoomPost> getRoomPostsByAgentId(@Param("agentId")Long agentId);
}
