package com.thiha.roomrent.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thiha.roomrent.dto.MrtLineDto;
import com.thiha.roomrent.dto.MrtLineRequest;
import com.thiha.roomrent.model.MrtLine;
import com.thiha.roomrent.model.MrtStation;
import com.thiha.roomrent.repository.MrtLineRepository;
import com.thiha.roomrent.repository.StationRepository;
import com.thiha.roomrent.service.MrtLineService;

@Service
public class MrtLineServiceImpl implements MrtLineService{

    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private MrtLineRepository mrtLineRepository;

    @Override
    public List<String> getStationNamesByMrtLineId(Long id) {
        List<MrtStation> stations = stationRepository.getStationsByMrtLineId(id);
        return stations.stream().map(station -> station.getName()).collect(Collectors.toList());
    }

    @Override
    public MrtLineDto createMrtLine(MrtLineRequest request) {
        MrtLine newLine = new MrtLine();
        newLine.setName(request.getName());
        try{
            newLine = mrtLineRepository.save(newLine);
        }catch(Exception e){
            e.printStackTrace();
        }
        MrtLineDto lineDto = new MrtLineDto();
        lineDto.setId(newLine.getId());
        lineDto.setLineName(newLine.getName());
        lineDto.setStationNames(null);
        return lineDto;
    }

    @Override
    public MrtLineDto getMrtLineByname(String mrtLineName) {
        Optional<MrtLine> optionalLine = mrtLineRepository.findByLineName(mrtLineName);
        if (optionalLine.isPresent()) {
            MrtLine line = optionalLine.get();
            MrtLineDto lineDto = new MrtLineDto();
            lineDto.setId(line.getId());
            lineDto.setLineName(line.getName());
            lineDto.setStationNames(line.getStations().stream().map(station-> station.getName()).collect(Collectors.toSet()));
            return lineDto;
        }
        return null;
    }

    @Override
    public MrtLineDto addNewStationToExistingLine(Long lineId, String newStationName) {
        Optional<MrtLine> optionalLine = mrtLineRepository.findById(lineId);
        if(optionalLine.isPresent()){
            MrtLine line = optionalLine.get();
            saveNewStation(newStationName, line);
            MrtLineDto lineDto = getMrtLineDto(line);
            return lineDto;
        }
        return null;
    }

    private void saveNewStation(String newStationName, MrtLine existingLine){
        MrtStation newStation = new MrtStation();
        Set<MrtLine> belongedLines = new HashSet<>();
        belongedLines.add(existingLine);
        newStation.setMrtLine(belongedLines);
        newStation.setName(newStationName);
        stationRepository.save(newStation);
    }

    private MrtLineDto getMrtLineDto(MrtLine mrtLine){
        MrtLineDto lineDto = new MrtLineDto();
        lineDto.setId(mrtLine.getId());
        lineDto.setLineName(mrtLine.getName());
        lineDto.setStationNames(mrtLine.getStations().stream().map(station -> station.getName()).collect(Collectors.toSet()));
        return lineDto;
    }
}
