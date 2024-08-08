package com.thiha.roomrent.mapper;

import com.thiha.roomrent.dto.LocationDto;
import com.thiha.roomrent.model.Location;

public class LocationMapper {
    public static LocationDto mapToLocationDto(Location location){
        LocationDto dto = new LocationDto();
        dto.setId(location.getId());
        dto.setName(location.getName());
        return dto;
    }
}
