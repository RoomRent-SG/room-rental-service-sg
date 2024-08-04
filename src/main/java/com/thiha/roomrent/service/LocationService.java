package com.thiha.roomrent.service;

import java.util.List;

import com.thiha.roomrent.dto.LocationDto;
import com.thiha.roomrent.dto.LocationRequest;

public interface LocationService {
    LocationDto createLocation(LocationRequest request);
    LocationDto updateLocation(LocationRequest request, Long locationId);
    List<LocationDto> getAllLocations();
    LocationDto getLocation(Long locationId);
    void deleteLocation(Long locationId);
}
