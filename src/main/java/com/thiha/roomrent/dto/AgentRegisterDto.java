package com.thiha.roomrent.dto;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thiha.roomrent.enums.UserRole;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class AgentRegisterDto {
   @JsonIgnore
   private Long id;
   @NotNull(message = "Email should not be empty")
   @NotBlank(message = "Email should not be empty")
   private String email;
   @NotNull(message = "Username should not be empty")
   @NotEmpty(message = "Username should not be empty")
   private String username;
   @NotBlank(message = "Password should not be empty")
   @NotNull(message = "Password should not be empty")
   @Size(min = 8, message = "Password should contains at least 8 characters")
   private String password;
   @NotBlank(message = "Phone number should not be empty")
   @NotNull(message = "Phone number should not be empty")
   private String phoneNumber;
   /*
    * To build custom validator for multiple part file
    */
   private MultipartFile profileImage;
   @JsonIgnore
   private String profilePhoto;
   @JsonIgnore
   private Date createdAt;
   @JsonIgnore
   private UserRole role;
}
 

