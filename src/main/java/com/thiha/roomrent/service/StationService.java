package com.thiha.roomrent.service;

import java.util.Set;

import com.thiha.roomrent.dto.StationRequest;

public interface StationService {
    Set<String> getAllStationNames();
    String createStation(StationRequest request, Long mrtLineId);
}
