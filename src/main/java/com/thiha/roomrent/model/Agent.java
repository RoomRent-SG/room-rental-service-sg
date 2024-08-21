package com.thiha.roomrent.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thiha.roomrent.enums.UserRole;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "agents", indexes = @Index(columnList = "email"))
public class Agent extends UserModel {

     @Column(name = "email", nullable = false)
     private String email;

     @Column(name = "phone_number", nullable = false)
     private String phoneNumber;

     @Column(name = "profile_photo", nullable = false)
     private String profilePhoto;

     @Column(name = "created_at", nullable = false)
     private Date createdAt;

     @OneToOne(mappedBy = "agent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
     @JsonIgnore
     private ConfirmationToken confirmationToken;


     public Agent(Long id, String username, String password, UserRole role,
               List<JwtToken> tokens, String email, String phoneNumber,
               String profilePhoto, Date createdAt,
               boolean isEnabled, ConfirmationToken confirmationToken) {
          super(id, username, password, role, isEnabled, tokens);
          this.email = email;
          this.phoneNumber = phoneNumber;
          this.profilePhoto = profilePhoto;
          this.createdAt = createdAt;
          this.confirmationToken = confirmationToken;
     }

}
