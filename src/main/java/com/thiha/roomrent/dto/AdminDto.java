package com.thiha.roomrent.dto;

import com.thiha.roomrent.enums.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AdminDto {
   private Long id;
   private String username;
   private UserRole role;
}
