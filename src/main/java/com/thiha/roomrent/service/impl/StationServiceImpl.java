package com.thiha.roomrent.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thiha.roomrent.dto.StationDto;
import com.thiha.roomrent.dto.StationRequest;
import com.thiha.roomrent.exceptions.EntityNotFoundException;
import com.thiha.roomrent.mapper.StationMapper;
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
    public StationDto createStation(StationRequest request, Long mrtLineId) {
        MrtLine mrtLine = mrtLineRepository.findById(mrtLineId).orElseThrow();

        MrtStation station = new MrtStation();
        station.setName(request.getStationName());
        Set<MrtLine> mrtLines = new HashSet<>();
        mrtLines.add(mrtLine);
        station.setMrtLine(mrtLines);
        stationRepository.save(station);

        mrtLine.getStations().add(station);
        mrtLineRepository.save(mrtLine);
        return StationMapper.mapToStationDto(station);
    }

    @Override
    public String getStationByName(String stationName) {
        Optional<MrtStation> optionalStation = stationRepository.getStationByName(stationName);
        if(optionalStation.isPresent()){
            MrtStation station = optionalStation.get();
            return station.getName();
        }
        throw new EntityNotFoundException("Station not found");
    }

    @Override
    public StationDto getStationById(Long id) {
        Optional<MrtStation> optioanlStation = stationRepository.findById(id);
        if(optioanlStation.isPresent()){
            MrtStation station = optioanlStation.get();
            return StationMapper.mapToStationDto(station);
        }
        throw new EntityNotFoundException("Station not found");
    }

    @Override
    public StationDto updateStation(StationRequest request, Long stationId) {
        Optional<MrtStation> optionalStation = stationRepository.findById(stationId);
        if (optionalStation.isPresent()) {
            MrtStation station = optionalStation.get();
            station.setName(request.getStationName());
            MrtStation updatedStation = stationRepository.save(station);
            return StationMapper.mapToStationDto(updatedStation);
        }
        throw new EntityNotFoundException("Station Not Found");
    }
    
}
