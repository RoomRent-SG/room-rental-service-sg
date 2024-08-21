package com.thiha.roomrent.model;


import java.util.List;

import com.thiha.roomrent.enums.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;


@Entity
@Table(name = "admins")
public class Admin extends UserModel{

   public Admin(){
      super();
   }

   public Admin(Long id, String username, String password, UserRole role, boolean isEnabled, List<JwtToken> tokens){
        super(id, username, password, role, isEnabled, tokens);
   }
}
