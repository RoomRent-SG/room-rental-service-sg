package com.thiha.roomrent.service;

import org.springframework.security.authentication.AuthenticationManager;

import com.thiha.roomrent.dto.LoginRequestDto;
import com.thiha.roomrent.dto.LoginResponseDto;

import jakarta.servlet.http.HttpServletResponse;

public interface LoginService {
    LoginResponseDto performLogin(LoginRequestDto loginRequestDto, AuthenticationManager authenticationManager, HttpServletResponse response);
}
