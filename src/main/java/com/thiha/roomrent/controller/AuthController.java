package com.thiha.roomrent.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.thiha.roomrent.dto.AdminDto;
import com.thiha.roomrent.dto.AdminRegister;
import com.thiha.roomrent.dto.AgentDto;
import com.thiha.roomrent.dto.AgentRegisterDto;
import com.thiha.roomrent.dto.ConfirmationTokenDto;
import com.thiha.roomrent.dto.LoginRequestDto;
import com.thiha.roomrent.dto.LoginResponseDto;
import com.thiha.roomrent.exceptions.InvalidRegistrationConfirmationTokenException;
import com.thiha.roomrent.service.AdminService;
import com.thiha.roomrent.service.AgentService;
import com.thiha.roomrent.service.ConfirmationTokenService;
import com.thiha.roomrent.service.LoginService;
import com.thiha.roomrent.validator.ObjectValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
   private final AgentService agentService;
   private final AdminService adminService;
   private final AuthenticationManager authenticationManager;
   private final LoginService loginService;
   private final ObjectValidator<AgentRegisterDto> agentRegisterValidator;
   private final ConfirmationTokenService confirmationTokenService;

   @PostMapping("/agent/register")
   public ResponseEntity<AgentDto> registerAgent(@ModelAttribute AgentRegisterDto registeredAgent){
        agentRegisterValidator.doVaildation(registeredAgent);
        System.out.println("inside auth controller");
        AgentDto savedAgent =  agentService.createAgent(registeredAgent);
        return new ResponseEntity<>(savedAgent, HttpStatus.CREATED);
    }

    @PostMapping("/admin/register")
    public ResponseEntity<?> registerAdmin(@RequestBody AdminRegister request ){
      AdminDto adminDto = adminService.createAdmin(request);
      return new ResponseEntity<>(adminDto, HttpStatus.OK);
    }

    @GetMapping("/complete-registration")
    public ResponseEntity<?> completeRegistration(@RequestParam UUID token){
      ConfirmationTokenDto tokenDto = confirmationTokenService.getConfirmationToken(token);
      if ((confirmationTokenService.isTokenValid(tokenDto))) {
        AgentDto agentDto = agentService.enableAgent(token);
        return new ResponseEntity<>(agentDto, HttpStatus.OK);
      }else{
        throw new InvalidRegistrationConfirmationTokenException("Invalid Confirmation Token");
      }
    } 
  
   @PostMapping("/agent/login")
   public ResponseEntity<LoginResponseDto> loginAgent(@RequestBody LoginRequestDto loginDto, HttpServletResponse response){
        LoginResponseDto loginResponse = loginService.performLogin(loginDto, authenticationManager, response);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
   }

   // TODO To put token in header
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
