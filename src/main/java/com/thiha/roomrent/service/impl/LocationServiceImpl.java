package com.thiha.roomrent.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.thiha.roomrent.dto.LocationDto;
import com.thiha.roomrent.dto.LocationRequest;
import com.thiha.roomrent.service.LocationService;

@Service
public class LocationServiceImpl implements LocationService {

    @Override
    public LocationDto createLocation(LocationRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createLocation'");
    }

    @Override
    public LocationDto updateLocation(LocationRequest request, Long locationId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateLocation'");
    }

    @Override
    public List<LocationDto> getAllLocations() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllLocations'");
    }

    @Override
    public LocationDto getLocation(Long locationId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLocation'");
    }

    @Override
    public void deleteLocation(Long locationId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteLocation'");
    }
    
}
