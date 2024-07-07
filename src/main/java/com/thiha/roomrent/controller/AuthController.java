package com.thiha.roomrent.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.thiha.roomrent.dto.AgentDto;
import com.thiha.roomrent.dto.AgentRegisterDto;
import com.thiha.roomrent.dto.LoginRequestDto;
import com.thiha.roomrent.dto.LoginResponseDto;
import com.thiha.roomrent.service.AgentService;
import com.thiha.roomrent.service.LoginService;
import com.thiha.roomrent.validator.ObjectValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
   private final AgentService agentService;
   private final AuthenticationManager authenticationManager;
   private final LoginService loginService;
   private final ObjectValidator<AgentRegisterDto> agentRegisterValidator;

   @PostMapping("/agent/register")
   public ResponseEntity<?> registerAgent(@ModelAttribute AgentRegisterDto registeredAgent){
        agentRegisterValidator.doVaildation(registeredAgent);
        AgentDto savedAgent =  agentService.createAgent(registeredAgent);
        return new ResponseEntity<>(savedAgent, HttpStatus.CREATED);
    }

  
   @PostMapping("/agent/login")
   public ResponseEntity<LoginResponseDto> loginAgent(@RequestBody LoginRequestDto loginDto, HttpServletResponse response){
        LoginResponseDto loginResponse = loginService.performLogin(loginDto, authenticationManager, response);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
   }

   @PostMapping("/admin/login")
   public ResponseEntity<LoginResponseDto> loginAdmin(@RequestBody LoginRequestDto loginDto, HttpServletResponse response){
     LoginResponseDto loginResponse = loginService.performLogin(loginDto, authenticationManager, response);
     return new ResponseEntity<LoginResponseDto>(loginResponse, HttpStatus.OK);
   }

   @GetMapping("/refresh")
   public ResponseEntity<LoginResponseDto> refreshToken(HttpServletRequest request,HttpServletResponse response){
     LoginResponseDto loginResponse = loginService.refreshToken(request, response);
     return new ResponseEntity<>(loginResponse, HttpStatus.OK);
   }
}
