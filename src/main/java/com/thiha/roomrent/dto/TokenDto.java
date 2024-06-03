package com.thiha.roomrent.dto;

import com.thiha.roomrent.model.UserModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {
    private Long id;
    private String token;
    private boolean isRevoked;
    private UserModel user;
}
