package com.thiha.roomrent.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import com.thiha.roomrent.enums.UserRole;
import com.thiha.roomrent.model.JwtToken;
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
   private String email;
   private String username;
   private String password;
   private String phoneNumber;
   private String profilePhoto;
   private Date createdAt;
   private UserRole role;
   private boolean isEnabled;
   private List<JwtToken> tokens;
}
