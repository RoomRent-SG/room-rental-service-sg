package com.thiha.roomrent.service;

import java.util.List;

import com.thiha.roomrent.dto.MrtLineDto;
import com.thiha.roomrent.dto.MrtLineRequest;

public interface MrtLineService {
    List<String> getStationNamesByMrtLineId(Long id);
    MrtLineDto createMrtLine(MrtLineRequest request);
    MrtLineDto getMrtLineByname(String mrtLineName);
    MrtLineDto addNewStationToExistingLine(Long lineId, String newStationName);
}
