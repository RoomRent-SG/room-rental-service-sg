package com.thiha.roomrent.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.thiha.roomrent.model.MrtLine;

@Repository
public interface MrtLineRepository extends JpaRepository<MrtLine,Long>{
    @Query("""
            SELECT line
            FROM MrtLine line
            WHERE line.name = :mrtLineName
            """)
    Optional<MrtLine> findByLineName(@Param("mrtLineName")String mrtLineName);
    
}
