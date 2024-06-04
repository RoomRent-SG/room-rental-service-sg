package com.thiha.roomrent.service.impl;


import com.thiha.roomrent.dto.TokenDto;
import com.thiha.roomrent.model.JwtToken;

public interface JwtTokenService {
    TokenDto saveToken(JwtToken token);
    TokenDto getTokenUsingTokenValue(String tokenString);
}
