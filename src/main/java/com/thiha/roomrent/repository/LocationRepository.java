package com.thiha.roomrent.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thiha.roomrent.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long>{
    Optional<Location> findLocationByName(String name);
}
