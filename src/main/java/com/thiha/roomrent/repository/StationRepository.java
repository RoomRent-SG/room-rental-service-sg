package com.thiha.roomrent.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.thiha.roomrent.model.MrtStation;

@Repository
public interface StationRepository extends JpaRepository<MrtStation, Long>{
    
    @Query("""
            SELECT s
            FROM MrtStation s
            JOIN s.mrtLine line
            WHERE line.id = :id
            """)
    List<MrtStation> getStationsByMrtLineId(@Param("id")Long id);
}
