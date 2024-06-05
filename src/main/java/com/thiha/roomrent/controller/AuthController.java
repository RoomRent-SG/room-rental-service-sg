package com.thiha.roomrent.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.thiha.roomrent.auth.JwtUtils;
import com.thiha.roomrent.dto.AgentDto;
import com.thiha.roomrent.dto.AgentRegisterDto;
import com.thiha.roomrent.dto.LoginRequestDto;
import com.thiha.roomrent.dto.LoginResponseDto;
import com.thiha.roomrent.model.JwtToken;
import com.thiha.roomrent.model.UserModel;
import com.thiha.roomrent.security.UserDetailsImpl;
import com.thiha.roomrent.service.AgentService;
import com.thiha.roomrent.service.JwtTokenServiceImpl;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
   private final AgentService agentService;
   private final AuthenticationManager authenticationManager;
   private final JwtUtils jwtUtils;
   private final JwtTokenServiceImpl jwtTokenService;

   @PostMapping("/agent/register")
   private ResponseEntity<?> registerAgent(@ModelAttribute AgentRegisterDto registeredAgent){
        AgentDto savedAgent =  agentService.createAgent(registeredAgent);
        return new ResponseEntity<>(savedAgent, HttpStatus.CREATED);
    }


  
   @PostMapping("/agent/login")
   private ResponseEntity<LoginResponseDto> loginAgent(@RequestBody LoginRequestDto loginDto){
        System.out.println("inside login controller...");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        UserModel user = userDetails.getUser();
        String jwtToken = jwtUtils.generateJwtToken(loginDto);
        JwtToken token = new JwtToken();
        token.setToken(jwtToken);
        token.setUser(user);
        jwtTokenService.saveToken(token);
        return new ResponseEntity<>(new LoginResponseDto(loginDto.getUsername(), jwtToken), HttpStatus.OK);
   }

}
