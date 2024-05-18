package com.thiha.roomrent.model;

import com.thiha.roomrent.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
   private Long id;
   private String username;
   private String password;
   private UserRole role;
}
