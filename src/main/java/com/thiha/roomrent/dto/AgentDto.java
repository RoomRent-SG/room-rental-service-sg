package com.thiha.roomrent.dto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thiha.roomrent.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgentDto {
   private Long id;
   private String email;
   private String username;
   @JsonIgnore
   private String password;
   private String phoneNumber;
   private String profilePhoto;
   @JsonFormat(pattern = "dd/MM/yyyy")
   private Date createdAt;
   private UserRole role;
}
