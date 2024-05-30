package com.thiha.roomrent.dto;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;
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
   @JsonIgnore
   private Long id;
   private String email;
   private String username;
   private String password;
   private String phoneNumber;
   private MultipartFile profileImage;
   @JsonIgnore
   private String profilePhoto;
   @JsonIgnore
   private Date createdAt;
   @JsonIgnore
   private UserRole role;
}
 

