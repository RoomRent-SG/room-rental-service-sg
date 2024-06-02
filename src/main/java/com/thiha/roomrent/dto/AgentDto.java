package com.thiha.roomrent.dto;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thiha.roomrent.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgentDto implements Serializable{
   @JsonIgnore
   private Long id;
   @NonNull
   private String email;
   @NonNull
   private String username;
   @JsonIgnore
   private String password;
   @NonNull
   private String phoneNumber;
   @NonNull
   private String profilePhoto;
   @JsonIgnore
   private Date createdAt;
   @NonNull
   private UserRole role;
}
