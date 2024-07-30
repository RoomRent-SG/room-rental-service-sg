package com.thiha.roomrent.mapper;

import com.thiha.roomrent.dto.StationDto;
import com.thiha.roomrent.model.MrtStation;

public class StationMapper {
    public static StationDto mapToStationDto(MrtStation station){
        StationDto dto = new StationDto();
        dto.setId(station.getId());
        dto.setName(station.getName());
        return dto;
    }
}
