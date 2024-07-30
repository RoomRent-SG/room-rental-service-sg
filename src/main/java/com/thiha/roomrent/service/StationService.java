package com.thiha.roomrent.service;

import java.util.Set;

import com.thiha.roomrent.dto.StationDto;
import com.thiha.roomrent.dto.StationRequest;

public interface StationService {
    Set<String> getAllStationNames();
    StationDto createStation(StationRequest request, Long mrtLineId);
    String getStationByName(String stationName);
    StationDto getStationById(Long id);
    StationDto updateStation(StationRequest request, Long stationId);
}
