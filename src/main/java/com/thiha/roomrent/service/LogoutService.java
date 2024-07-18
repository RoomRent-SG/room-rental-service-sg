package com.thiha.roomrent.service;

import org.springframework.security.core.Authentication;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface LogoutService {
    void performLogout(HttpServletRequest request, HttpServletResponse response, Authentication authentication);

}
