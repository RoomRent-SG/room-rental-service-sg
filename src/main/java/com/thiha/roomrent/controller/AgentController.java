package com.thiha.roomrent.controller;


import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.thiha.roomrent.enums.UserRole;
import com.thiha.roomrent.mapper.AgentMapper;
import com.thiha.roomrent.model.Agent;
import com.thiha.roomrent.model.UserModel;
import com.thiha.roomrent.security.UserDetailsImpl;
import com.thiha.roomrent.service.AgentService;
import com.thiha.roomrent.service.LogoutService;
import com.thiha.roomrent.service.RoomPostService;
import com.thiha.roomrent.service.S3ImageService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;


@RestController
@RequestMapping("/api/agent")
@AllArgsConstructor
public class AgentController {
    private AgentService agentService;
    private RoomPostService roomPostService;
    private S3ImageService s3ImageService;
    private LogoutService logoutService;
    

    @GetMapping("/profile")
    private ResponseEntity<AgentDto> getAgent(){
        String currentUser = getCurrentAgentName();
        AgentDto agent = agentService.findAgentByName(currentUser);
        return new ResponseEntity<>(agent, HttpStatus.OK);
    }
   
    @PutMapping("/profile")
    private ResponseEntity<AgentDto> updateAgent(@ModelAttribute AgentRegisterDto newAgent){
        AgentDto existingAgent = getCurrentAgent();
        AgentDto updatedAgent = agentService.updateExistingAgent(newAgent, existingAgent);
        return new ResponseEntity<>(updatedAgent, HttpStatus.OK);
    }

    @PostMapping("/room-post")
    private ResponseEntity<RoomPostDto> createRoomPost(@ModelAttribute RoomPostRegisterDto registeredRoomPost){
        AgentDto currentAgent = getCurrentAgent(); 
        Agent agent = AgentMapper.mapToAgent(currentAgent);
        RoomPostDto savedRoomPost = roomPostService.createRoomPost(registeredRoomPost, agent);
        return new ResponseEntity<>(savedRoomPost, HttpStatus.CREATED);
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
        RoomPostDto updatedRoomPost = roomPostService.updateRoomPost(id, currentAgent, editedRoomPost);
        return new ResponseEntity<>(updatedRoomPost, HttpStatus.OK);
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

    @PostMapping("/logout")
    private ResponseEntity<Void> doLogout(HttpServletRequest request,
                 HttpServletResponse response
                 ,Authentication authentication){
                    System.out.println("log out controller.");
        try{
            logoutService.performLogout(request, response, authentication);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


    //utility methods
    private AgentDto getCurrentAgent(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl)authentication.getPrincipal();
        UserModel user = userDetails.getUser();
        if (user.getRole() == UserRole.AGENT && user instanceof Agent) {
            return AgentMapper.mapToAgentDto((Agent) user);
        }
        return null;
    }

    private String getCurrentAgentName(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl)authentication.getPrincipal();
        return userDetails.getUsername();
    }
        
}
