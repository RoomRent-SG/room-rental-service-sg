package com.thiha.roomrent.controller;


import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.thiha.roomrent.dto.AgentDto;
import com.thiha.roomrent.dto.AgentRegisterDto;
import com.thiha.roomrent.dto.RoomPostDto;
import com.thiha.roomrent.dto.RoomPostRegisterDto;
import com.thiha.roomrent.mapper.AgentMapper;
import com.thiha.roomrent.model.Agent;
import com.thiha.roomrent.service.AgentService;
import com.thiha.roomrent.service.RoomPostService;
import com.thiha.roomrent.service.S3ImageService;

import lombok.AllArgsConstructor;


@RestController
@RequestMapping("/api/agent")
@AllArgsConstructor
public class AgentController {
    private AgentService agentService;
    private RoomPostService roomPostService;
    private S3ImageService s3ImageService;
    

    @GetMapping("/profile")
    private ResponseEntity<AgentDto> getAgent(){
        String currentUser = getCurrentAgentName();
        AgentDto agent = agentService.findAgentByName(currentUser);
        if (agent != null) {
            return new ResponseEntity<>(agent, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       
    }
   
    @PutMapping("/profile")
    private ResponseEntity<AgentDto> updateAgent(@ModelAttribute AgentRegisterDto newAgent){
        String currentUser = getCurrentAgentName();

        if(newAgent.getProfileImage()==null || newAgent.getProfileImage().isEmpty()){
            // profile image must not be null
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        MultipartFile newProfileImage = newAgent.getProfileImage();

        AgentDto existingAgent = agentService.findAgentByName(currentUser);
        if (existingAgent != null) {
            /*agent can only change phone number and profile picture
            delete the exsiting image on s3 and update the profile photo name in db 
            */ 
            try{
                s3ImageService.deleteImage(existingAgent.getProfilePhoto());
                String filename = newProfileImage.getOriginalFilename();
                s3ImageService.uploadImage(filename, newProfileImage);
                newAgent.setProfilePhoto(filename);
                AgentDto updatedAgent = agentService.updateExistingAgent(AgentMapper.mapToAgentDtoFromAgentRegisterDto(newAgent), existingAgent);
                return new ResponseEntity<>(updatedAgent, HttpStatus.OK);
            }catch(IOException e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/room-post")
    private ResponseEntity<RoomPostDto> createRoomPost(@ModelAttribute RoomPostRegisterDto registeredRoomPost){
        AgentDto currentAgent = getCurrentAgent();
        List<MultipartFile> roomPhotoFiles = registeredRoomPost.getRoomPhotoFiles();
        if(roomPhotoFiles == null){
            // The room post creation requires room photos
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(roomPhotoFiles.size() > 10){
            // too many photos
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(currentAgent != null){
            try{
                Agent agent = AgentMapper.mapToAgent(currentAgent);
                RoomPostDto savedRoomPost = roomPostService.createRoomPost(registeredRoomPost, agent);
                return new ResponseEntity<>(savedRoomPost, HttpStatus.CREATED);
            }catch(RuntimeException e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/room-post")
    private ResponseEntity<List<RoomPostDto>> getRoomPosts(){
        AgentDto currentAgent = getCurrentAgent();
        List<RoomPostDto> roomPosts = roomPostService.getRoomPostsByAgentId(currentAgent.getId());
        return new ResponseEntity<>(roomPosts, HttpStatus.OK);
    }


    @GetMapping("/room-post/{id}")
    private ResponseEntity<RoomPostDto> getRoomPost(@PathVariable Long id){
        String currentUser = getCurrentAgentName();
        RoomPostDto roomPostDto = roomPostService.findRoomPostById(id);
        if(roomPostDto != null){
            if(roomPostDto.getAgent().getUsername().equals(currentUser)){
                return new ResponseEntity<>(roomPostDto, HttpStatus.OK);
            }
        }
        
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Edit existing room post
    @PutMapping("/room-post/{id}")
    private ResponseEntity<RoomPostDto> updateRoomPost(@ModelAttribute RoomPostRegisterDto editedRoomPost, @PathVariable Long id){
        AgentDto currentAgent = getCurrentAgent();
        RoomPostDto originalRoomPostDto = roomPostService.findRoomPostById(id);
        if(originalRoomPostDto != null){
            if(currentAgent.getUsername().equals(originalRoomPostDto.getAgent().getUsername())){
                RoomPostDto updatedRoomPost = roomPostService.updateRoomPost(originalRoomPostDto, editedRoomPost);
                return new ResponseEntity<>(updatedRoomPost, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/room-post/{roomPostId}")
    private ResponseEntity<Void> deleteRoomPost(@PathVariable Long roomPostId){
        AgentDto currentAgent = getCurrentAgent();
        RoomPostDto roomPostToDelete = roomPostService.findRoomPostById(roomPostId);
        if(roomPostToDelete == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(currentAgent.getId() == roomPostToDelete.getAgent().getId()){
            // Delete room post
            roomPostService.deleteRoomPostById(roomPostId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/{postId}/images")
    private ResponseEntity<Void> uploadRoomImage(@RequestParam("file") MultipartFile file){
        try{
            s3ImageService.uploadImage(file.getOriginalFilename(), file);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(IOException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
