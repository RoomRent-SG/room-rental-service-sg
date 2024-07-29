package com.thiha.roomrent.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thiha.roomrent.dto.StationRequest;
import com.thiha.roomrent.model.MrtLine;
import com.thiha.roomrent.model.MrtStation;
import com.thiha.roomrent.repository.MrtLineRepository;
import com.thiha.roomrent.repository.StationRepository;
import com.thiha.roomrent.service.StationService;

@Service
public class StationServiceImpl implements StationService{
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private MrtLineRepository mrtLineRepository;

    @Override
    public Set<String> getAllStationNames() {
        List<MrtStation> allStations = stationRepository.findAll();
        return allStations.stream().map(station -> station.getName()).collect(Collectors.toSet());
    }

    @Override
    public String createStation(StationRequest request, Long mrtLineId) {
        MrtLine mrtLine = mrtLineRepository.findById(mrtLineId).orElseThrow();
        
        MrtStation station = new MrtStation();
        station.setName(request.getStationName());
        Set<MrtLine> mrtLines = new HashSet<>();
        mrtLines.add(mrtLine);
        station.setMrtLine(mrtLines);
        stationRepository.save(station);

        mrtLine.getStations().add(station);
        mrtLineRepository.save(mrtLine);
        return station.getName();
    }
    
}
