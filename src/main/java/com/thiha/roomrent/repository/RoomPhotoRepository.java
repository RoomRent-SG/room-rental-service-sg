package com.thiha.roomrent.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thiha.roomrent.model.RoomPhoto;

public interface RoomPhotoRepository extends JpaRepository<RoomPhoto, Long>{
    
}
