package com.thiha.roomrent.controller;


import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RestController;
import com.thiha.roomrent.dto.AgentDto;
import com.thiha.roomrent.dto.AgentRegisterDto;
import com.thiha.roomrent.dto.RoomPostDto;
import com.thiha.roomrent.dto.RoomPostRegisterDto;
import com.thiha.roomrent.enums.UserRole;
import com.thiha.roomrent.exceptions.EntityNotFoundException;
import com.thiha.roomrent.mapper.AgentMapper;
import com.thiha.roomrent.model.Agent;
import com.thiha.roomrent.model.UserModel;
import com.thiha.roomrent.security.UserDetailsImpl;
import com.thiha.roomrent.service.AgentService;
import com.thiha.roomrent.service.LogoutService;
import com.thiha.roomrent.service.RoomPostService;
import com.thiha.roomrent.validator.ObjectValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;


@RestController
@RequestMapping("/api/v1/agent")
@AllArgsConstructor
public class AgentController {
    private AgentService agentService;
    private RoomPostService roomPostService;
    private LogoutService logoutService;
    private ObjectValidator<RoomPostRegisterDto> roomPostValidator;
    private ObjectValidator<AgentRegisterDto> agentValidator;
    

    @GetMapping("/profile")
    public ResponseEntity<AgentDto> getAgent(){
        String currentUser = getCurrentAgentName();
        AgentDto agent = agentService.findAgentByName(currentUser);
        return new ResponseEntity<>(agent, HttpStatus.OK);
    }
   
    @PutMapping("/profile")
    public ResponseEntity<AgentDto> updateAgent(@ModelAttribute AgentRegisterDto newAgent){
        agentValidator.doVaildation(newAgent);
        AgentDto existingAgent = getCurrentAgent();
        AgentDto updatedAgent = agentService.updateExistingAgent(newAgent, existingAgent);
        return new ResponseEntity<>(updatedAgent, HttpStatus.OK);
    }

    @PostMapping("/room-post")
    public ResponseEntity<?> createRoomPost(@ModelAttribute RoomPostRegisterDto registeredRoomPost){
        roomPostValidator.doVaildation(registeredRoomPost);
        AgentDto currentAgent = getCurrentAgent();
        RoomPostDto savedRoomPost = roomPostService.createRoomPost(registeredRoomPost, currentAgent);
        return new ResponseEntity<>(savedRoomPost, HttpStatus.CREATED);
    }

    @GetMapping("/room-post")
    public ResponseEntity<?> getRoomPostRegisterMetadata(){
        Map<String, Object> metaData = roomPostService.getRoomPostRegisterMetadata();
        return new ResponseEntity<>(metaData, HttpStatus.OK);
    }

    @GetMapping("/room-post/active")
    public ResponseEntity<List<RoomPostDto>> getActiveRoomPosts(){
        AgentDto currentAgent = getCurrentAgent();
        List<RoomPostDto> roomPosts = roomPostService.getActiveRoomPostsByAgentId(currentAgent.getId());
        return new ResponseEntity<>(roomPosts, HttpStatus.OK);
    }

    @GetMapping("/room-post/archived")
    public ResponseEntity<List<RoomPostDto>> getArchivedRoomPosts(){
        AgentDto currentAgent = getCurrentAgent();
        List<RoomPostDto> roomPosts = roomPostService.getArchivedRoomPostsByAgentId(currentAgent.getId());
        return new ResponseEntity<>(roomPosts, HttpStatus.OK);
    }



    @GetMapping("/room-post/{id}")
    public ResponseEntity<RoomPostDto> getRoomPost(@PathVariable Long id){
        String currentUser = getCurrentAgentName();
        RoomPostDto roomPostDto = roomPostService.findRoomPostById(id);
        if(roomPostDto.getAgent().getUsername().equals(currentUser)){
            return new ResponseEntity<>(roomPostDto, HttpStatus.OK);
        }else{
            // don't let the user know the unauthorized entity exists
            throw new EntityNotFoundException("Roompost cannot be found");
        }
    }

    // Edit existing room post
    @PutMapping("/room-post/{id}")
    public ResponseEntity<RoomPostDto> updateRoomPost(@ModelAttribute RoomPostRegisterDto editedRoomPost, @PathVariable Long id){
        AgentDto currentAgent = getCurrentAgent();
        roomPostValidator.doVaildation(editedRoomPost);
        RoomPostDto updatedRoomPost = roomPostService.updateRoomPost(id, currentAgent, editedRoomPost);
        return new ResponseEntity<>(updatedRoomPost, HttpStatus.OK);
    }

    @PutMapping("/room-post/{id}/activate")
    public ResponseEntity<Void> reactivateRoomPost(@PathVariable Long id){
        roomPostService.reactivateRoomPost(id, getCurrentAgent());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/room-post/{roomPostId}")
    public ResponseEntity<Void> deleteRoomPost(@PathVariable Long roomPostId){
        AgentDto currentAgent = getCurrentAgent();
        roomPostService.deleteRoomPostById(roomPostId, currentAgent);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/logout")
    public ResponseEntity<Void> doLogout(HttpServletRequest request,
                 HttpServletResponse response
                 ,Authentication authentication){
                    System.out.println("INSIDE CONTROLLER...");
        logoutService.performLogout(request, response, authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    //utility methods
    public AgentDto getCurrentAgent(){
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
