package com.thiha.roomrent.service;

import org.springframework.security.authentication.AuthenticationManager;

import com.thiha.roomrent.dto.LoginRequestDto;
import com.thiha.roomrent.dto.LoginResponseDto;

public interface LoginService {
    LoginResponseDto performLogin(LoginRequestDto loginRequestDto, AuthenticationManager authenticationManager);
}
