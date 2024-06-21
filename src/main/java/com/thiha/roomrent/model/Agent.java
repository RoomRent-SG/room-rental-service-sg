package com.thiha.roomrent.model;

import java.util.Date;
import java.util.List;
import com.thiha.roomrent.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "agents")
public class Agent extends UserModel{
    // @Id
    // @JsonIgnore
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    // private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    // @Column(name = "user_name", nullable = false)
    // private String username;

    // @Column(name = "password")
    // private String password;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "profile_photo", nullable = false)
    private String profilePhoto;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

   public Agent(Long id, String username, String password, UserRole role,
   List<JwtToken> tokens, String email, String phoneNumber, String profilePhoto, Date createdAt){
        super(id, username, password, role, tokens);
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profilePhoto = profilePhoto;
        this.createdAt = createdAt;
   }

}
