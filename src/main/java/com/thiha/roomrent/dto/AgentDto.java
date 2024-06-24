package com.thiha.roomrent.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thiha.roomrent.enums.UserRole;
import com.thiha.roomrent.model.JwtToken;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentDto implements Serializable{
   private Long id;
   @NotNull(message = "Email should not be empty")
   private String email;
   @NotNull(message = "Username should not be empty")
   @NotBlank(message = "Username should not be empty")
   private String username;
   @JsonIgnore
   private String password;
   @NotNull(message = "Phone number should not be empty")
   @NotEmpty(message = "Phone number should not be empty")
   private String phoneNumber;
   @NotNull(message = "profile photo should not be empty")
   private String profilePhoto;
   @JsonIgnore
   private Date createdAt;
   @NotNull(message = "Role should not be empty")
   private UserRole role;
   @JsonIgnore
   private List<JwtToken> tokens;
}
