package com.thiha.roomrent.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thiha.roomrent.auth.JwtUtils;
import com.thiha.roomrent.dto.AgentDto;
import com.thiha.roomrent.dto.LoginRequestDto;
import com.thiha.roomrent.dto.LoginResponseDto;
import com.thiha.roomrent.enums.UserRole;
import com.thiha.roomrent.service.AgentService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
   private final AgentService agentService;
   private final AuthenticationManager authenticationManager;
   private final JwtUtils jwtUtils;

   @PostMapping("/agent/register")
   private ResponseEntity<?> registerAgent(@RequestBody AgentDto agentDto){
        AgentDto agentByEmail = agentService.findAgentByEmail(agentDto.getEmail());
        AgentDto agentByName = agentService.findAgentByName(agentDto.getUsername());
        if(agentByEmail == null && agentByName == null){
            agentDto.setRole(UserRole.AGENT);
            AgentDto savedAgentDto = agentService.createAgent(agentDto);
            return new ResponseEntity<>(savedAgentDto, HttpStatus.CREATED);
        }else if(agentByEmail!=null){
            return new ResponseEntity<>("Email already registered",HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>("Name already exits", HttpStatus.BAD_REQUEST);
        }
   }


  
   @PostMapping("/agent/login")
   private ResponseEntity<LoginResponseDto> loginAgent(@RequestBody LoginRequestDto loginDto){
        System.out.println("inside login controller...");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtUtils.generateJwtToken(loginDto);
        return new ResponseEntity<>(new LoginResponseDto(loginDto.getUsername(), jwtToken), HttpStatus.OK);
   }
}
