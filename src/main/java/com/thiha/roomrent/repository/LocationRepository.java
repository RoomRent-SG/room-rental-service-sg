package com.thiha.roomrent.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thiha.roomrent.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long>{
    
}
