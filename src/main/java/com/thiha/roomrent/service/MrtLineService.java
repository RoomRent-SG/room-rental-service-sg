package com.thiha.roomrent.service;

import java.util.List;

import com.thiha.roomrent.dto.MrtLineDto;
import com.thiha.roomrent.dto.MrtLineRequest;
import com.thiha.roomrent.dto.StationDto;

public interface MrtLineService {
    List<StationDto> getStationsByMrtLineId(Long id);
    MrtLineDto createMrtLine(MrtLineRequest request);
    MrtLineDto getMrtLineByname(String mrtLineName);
    MrtLineDto addNewStationToExistingLine(Long lineId, String newStationName);
    void deleteMrtLineById(Long id);
}
