package com.thiha.roomrent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thiha.roomrent.model.RoomPhoto;

@Repository
public interface RoomPhotoRepository extends JpaRepository<RoomPhoto, Long>{
    
}
