package com.thiha.roomrent.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.thiha.roomrent.model.RoomPost;

@Repository
public interface RoomPostRepository extends JpaRepository<RoomPost, Long>, JpaSpecificationExecutor<RoomPost>{

    @Query("SELECT roomPost FROM RoomPost roomPost WHERE roomPost.agent.id = :agentId")
   List<RoomPost> findAllRoomPostsByAgentId(@Param("agentId")Long agentId);

   @Query("SELECT roomPost FROM RoomPost roomPost WHERE roomPost.agent.id = :agentId AND roomPost.isArchived=false")
   Page<RoomPost> findActiveRoomPostsByAgentId(@Param("agentId")Long agentId, Pageable pageable);

   @Query("SELECT roomPost FROM RoomPost roomPost WHERE roomPost.agent.id = :agentId AND roomPost.isArchived=true")
   List<RoomPost> findArchivedRoomPostsByAgentId(@Param("agentId")Long agentId);
   
   
   @SuppressWarnings("null")
    Page<RoomPost> findAll(Specification<RoomPost> specification,Pageable pageable);

    /*
     * Specification api cannot work with custom query
     */
   @Query("SELECT roomPost FROM RoomPost roomPost WHERE roomPost.isArchived=false")
   Page<RoomPost> findAllActiveRoomPosts(Pageable pageable);

   @Query("SELECT roomPost FROM RoomPost roomPost WHERE roomPost.isArchived=false")
   List<RoomPost> findAllActiveRoomPosts();

}
