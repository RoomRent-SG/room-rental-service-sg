package com.thiha.roomrent.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.thiha.roomrent.dto.AgentDto;
import com.thiha.roomrent.service.AgentService;
import com.thiha.roomrent.service.LogoutService;
import com.thiha.roomrent.service.RoomPostService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AgentService agentService;

    @Autowired
    private LogoutService logoutService;


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
}
