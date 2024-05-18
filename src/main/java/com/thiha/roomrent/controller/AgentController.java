package com.thiha.roomrent.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.thiha.roomrent.dto.AgentDto;
import com.thiha.roomrent.dto.RoomPostDto;
import com.thiha.roomrent.mapper.AgentMapper;
import com.thiha.roomrent.model.Agent;
import com.thiha.roomrent.service.AgentService;
import com.thiha.roomrent.service.RoomPostService;

import lombok.AllArgsConstructor;


@RestController
@RequestMapping("/api/agent")
@AllArgsConstructor
public class AgentController {
    private AgentService agentService;
    private RoomPostService roomPostService;
    

    @GetMapping("/profile")
    private ResponseEntity<AgentDto> getAgent(){
        String currentUser = getCurrentAgentName();
        System.out.println(currentUser+ " in agent controller");
        AgentDto agent = agentService.findAgentByName(currentUser);
        if (agent != null) {
            return new ResponseEntity<>(agent, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       
    }
   
    @PutMapping("/edit-profile")
    private ResponseEntity<AgentDto> updateAgent(@RequestBody AgentDto newAgent){
        String currentUser = getCurrentAgentName();

        AgentDto existingAgent = agentService.findAgentByName(currentUser);
        if (existingAgent != null) {
            // agent can only change phone number and profile picture
            AgentDto updatedAgent = agentService.updateExistingAgent(newAgent, existingAgent);
            return new ResponseEntity<>(updatedAgent, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/add-post")
    private ResponseEntity<RoomPostDto> createRoomPost(@RequestBody RoomPostDto roomPostDto){
        AgentDto currentAgent = getCurrentAgent();
        if(currentAgent != null){
            Agent agent = AgentMapper.mapToAgent(currentAgent);
            RoomPostDto savedRoomPost = roomPostService.createRoomPost(roomPostDto, agent);
            return new ResponseEntity<>(savedRoomPost, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/room-post/{id}")
    private ResponseEntity<RoomPostDto> getRoomPost(@PathVariable Long id){
        String currentUser = getCurrentAgentName();
        RoomPostDto roomPostDto = roomPostService.findRoomPostById(id);
        if(roomPostDto != null){
            if(roomPostDto.getAgent().getUsername().equals(currentUser)){
                return new ResponseEntity<>(roomPostDto, HttpStatus.OK);
            }
        }else{
            System.out.println("Reach here because roomPost is null..");
        }
        
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/edit-post/{id}")
    private ResponseEntity<RoomPostDto> updateRoomPost(@RequestBody RoomPostDto roomPostDto, @PathVariable Long id){
        AgentDto currentAgent = getCurrentAgent();
        RoomPostDto originalRoomPostDto = roomPostService.findRoomPostById(id);
        if(originalRoomPostDto != null){
            if(currentAgent.getUsername().equals(originalRoomPostDto.getAgent().getUsername())){
                RoomPostDto updatedRoomPost = roomPostService.updateRoomPost(originalRoomPostDto, roomPostDto);
                return new ResponseEntity<>(updatedRoomPost, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    //utility methods
    private AgentDto getCurrentAgent(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUser;
        if(principal instanceof UserDetails){
            currentUser = ((UserDetails)principal).getUsername();
        }else{
            currentUser = principal.toString();
        }
        AgentDto agent = agentService.findAgentByName(currentUser);
        return agent;
    }

    private String getCurrentAgentName(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUser;
        if(principal instanceof UserDetails){
            currentUser = ((UserDetails)principal).getUsername();
        }else{
            currentUser = principal.toString();
        }
        return currentUser;
    }
}
