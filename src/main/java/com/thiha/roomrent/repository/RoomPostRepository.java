package com.thiha.roomrent.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thiha.roomrent.model.RoomPost;

public interface RoomPostRepository extends JpaRepository<RoomPost, Long>{
   
}
