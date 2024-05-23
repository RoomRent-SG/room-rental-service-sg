package com.thiha.roomrent.controller;

import java.io.IOException;
import java.util.Arrays;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.thiha.roomrent.auth.JwtUtils;
import com.thiha.roomrent.dto.AgentDto;
import com.thiha.roomrent.dto.AgentRegisterDto;
import com.thiha.roomrent.dto.LoginRequestDto;
import com.thiha.roomrent.dto.LoginResponseDto;
import com.thiha.roomrent.enums.UserRole;
import com.thiha.roomrent.mapper.AgentMapper;
import com.thiha.roomrent.service.AgentService;
import com.thiha.roomrent.service.S3ImageUploadService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
   private final AgentService agentService;
   private final AuthenticationManager authenticationManager;
   private final JwtUtils jwtUtils;
   private final S3ImageUploadService imageUploadService;

   @PostMapping("/agent/register")
   private ResponseEntity<?> registerAgent(@ModelAttribute AgentRegisterDto registeredAgent){
        AgentDto agentByEmail = agentService.findAgentByEmail(registeredAgent.getEmail());
        AgentDto agentByName = agentService.findAgentByName(registeredAgent.getUsername());
        if(agentByEmail == null && agentByName == null){
            // if registering agent doesn't have profile photo, the request should not be handled
            if(registeredAgent.getProfileImage() == null || registeredAgent.getProfileImage().isEmpty()){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            MultipartFile profileImage = registeredAgent.getProfileImage();
            try{
                imageUploadService.uploadImage(profileImage.getOriginalFilename(), profileImage);
            }catch(IOException e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            // save filename or url of s3 object
            registeredAgent.setProfilePhoto(profileImage.getOriginalFilename());
            registeredAgent.setRole(UserRole.AGENT);
            AgentDto savedAgentDto = agentService.createAgent(AgentMapper.mapToAgentDtoFromAgentRegisterDto(registeredAgent));
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

   //test
    @PostMapping("/images")
    private ResponseEntity<Void> uploadRoomImage(@RequestParam("files") MultipartFile[] files){
        try{
            Arrays.asList(files).stream().forEach(file ->{
                try {
                    imageUploadService.uploadImage(file.getOriginalFilename(), file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(RuntimeException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
