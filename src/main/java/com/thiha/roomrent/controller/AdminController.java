package com.thiha.roomrent.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.thiha.roomrent.dto.AgentDto;
import com.thiha.roomrent.dto.MrtLineDto;
import com.thiha.roomrent.dto.MrtLineRequest;
import com.thiha.roomrent.dto.StationDto;
import com.thiha.roomrent.dto.StationRequest;
import com.thiha.roomrent.service.AgentService;
import com.thiha.roomrent.service.LogoutService;
import com.thiha.roomrent.service.MrtLineService;
import com.thiha.roomrent.service.StationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AgentService agentService;

    @Autowired
    private LogoutService logoutService;
    @Autowired
    private MrtLineService mrtLineService;
    @Autowired
    private StationService stationService;


    @GetMapping("/agents")
    public ResponseEntity<List<AgentDto>> getAllAgents(){
        List<AgentDto> agents = agentService.findAllAgents();
        return new ResponseEntity<List<AgentDto>>(agents, HttpStatus.OK);
    }


    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
        HttpServletRequest request, 
        HttpServletResponse response,
        Authentication authentication
    ){
        logoutService.performLogout(request, response, authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/mrt-line")
    public ResponseEntity<MrtLineDto> createNewMrtLine( @RequestBody MrtLineRequest request){
        MrtLineDto savedLine = mrtLineService.createMrtLine(request);
        return new ResponseEntity<>(savedLine, HttpStatus.OK);
    }

    @GetMapping("/mrt-line/{lineId}/stations")
    public ResponseEntity<List<StationDto>> getStationsByMrtLine(@PathVariable Long lineId){
        List<StationDto> stations = mrtLineService.getStationsByMrtLineId(lineId);
        return new ResponseEntity<>(stations, HttpStatus.OK);
    }

    @DeleteMapping("/mrt-line/{lineId}")
    public ResponseEntity<Void> deleteMrtLine(@PathVariable Long lineId){
        mrtLineService.deleteMrtLineById(lineId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/mrt-line/{id}/station")
    public ResponseEntity<StationDto> createNewStation(@PathVariable("id") Long mrtLineId,
                        @RequestBody StationRequest request){
        StationDto createdStation = stationService.createStation(request, mrtLineId);
        return new ResponseEntity<>(createdStation, HttpStatus.OK);
    }

    @GetMapping("/station/{stationId}")
    public ResponseEntity<StationDto> getStation(
        @PathVariable Long stationId
    ){
        StationDto stationDto = stationService.getStationById(stationId);
        return new ResponseEntity<>(stationDto,HttpStatus.OK);
    }

    @PutMapping("/station/{stationId}")
    public ResponseEntity<StationDto> editStation(
        @PathVariable Long stationId,
        @RequestBody StationRequest request
    ){
        StationDto station = stationService.updateStation(request, stationId);
        return new ResponseEntity<>(station, HttpStatus.CREATED);
    }

}
