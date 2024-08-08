package com.thiha.roomrent.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.thiha.roomrent.dto.LocationDto;
import com.thiha.roomrent.dto.LocationRequest;
import com.thiha.roomrent.exceptions.EntityNotFoundException;
import com.thiha.roomrent.mapper.LocationMapper;
import com.thiha.roomrent.model.Location;
import com.thiha.roomrent.repository.LocationRepository;
import com.thiha.roomrent.service.LocationService;

@Service
public class LocationServiceImpl implements LocationService {
    @Autowired
    private LocationRepository locationRepository;

    @Override
    public LocationDto createLocation(LocationRequest request) {
        Location newLocation = new Location();
        newLocation.setName(request.getName());
        System.out.println(request.getName());
        newLocation.setRoomPosts(null);
        Location savedLocation = locationRepository.save(newLocation);
        return LocationMapper.mapToLocationDto(savedLocation);
    }

    @Override
    public LocationDto updateLocation(LocationRequest request, Long locationId) {
        Optional<Location> optionalLocation = locationRepository.findById(locationId);
        if(!optionalLocation.isPresent()){
            throw new EntityNotFoundException("Location not found");
        }
        Location location = optionalLocation.get();
        location.setName(request.getName());
        Location updateLocation = locationRepository.save(location);
        return LocationMapper.mapToLocationDto(updateLocation);
    }

    @Override
    public List<LocationDto> getAllLocations() {
        List<Location> locations = locationRepository.findAll();
        return locations.stream().map(location -> LocationMapper.mapToLocationDto(location)).collect(Collectors.toList());
    }

    @Override
    public LocationDto getLocation(Long locationId) {
        Optional<Location> optionalLocation = locationRepository.findById(locationId);
        if(!optionalLocation.isPresent()){
            throw new EntityNotFoundException("Location not found");
        }
        return LocationMapper.mapToLocationDto(optionalLocation.get());
    }

    @Override
    public void deleteLocation(Long locationId) {
        Optional<Location> optionalLocation = locationRepository.findById(locationId);
        if(!optionalLocation.isPresent()){
            throw new EntityNotFoundException("Location not found");
        }
        Location location = optionalLocation.get();
        locationRepository.delete(location);
    }

    @Override
    public LocationDto getLocationByName(String location) {
        Optional<Location> optionalLocation  = locationRepository.findLocationByName(location);
        if(!optionalLocation.isPresent()){
            throw new EntityNotFoundException("Location not found");
        }
        return LocationMapper.mapToLocationDto(optionalLocation.get());
    }
    
}
