package com.thiha.roomrent.dto;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thiha.roomrent.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/*
 * Object to handle agent registeration 
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgentRegisterDto {
   private Long id;
   private String email;
   private String username;
   private String password;
   private String phoneNumber;
   @JsonIgnore
   private String profilePhoto;
   private MultipartFile profileImage;
   @JsonFormat(pattern = "dd/MM/yyyy")
   private Date createdAt;
   private UserRole role;
}
 

