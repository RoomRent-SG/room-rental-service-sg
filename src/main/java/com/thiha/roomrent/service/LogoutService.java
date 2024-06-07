package com.thiha.roomrent.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import com.thiha.roomrent.auth.JwtUtils;
import com.thiha.roomrent.dto.TokenDto;
import com.thiha.roomrent.exceptions.LogoutException;
import com.thiha.roomrent.mapper.TokenMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class LogoutService {
   private final SecurityContextLogoutHandler logoutHandler;
   private final JwtTokenServiceImpl tokenService;
   private final JwtUtils jwtUtils;

   public LogoutService(JwtTokenServiceImpl tokenService,
                        JwtUtils jwtUtils){
        this.logoutHandler = new SecurityContextLogoutHandler();
        this.tokenService = tokenService;
        this.jwtUtils = jwtUtils;
   }

   public void performLogout(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        String stringToken = jwtUtils.extractTokenFromRequest(request);
        if(stringToken!=null){
            TokenDto token = tokenService.getTokenUsingTokenValue(stringToken);
            System.out.println(stringToken);
            if(token !=null){
                token.setRevoked(true);
                tokenService.saveToken(TokenMapper.mapToJwtToken(token));
                System.out.println("gotToken..");
                // clean up 
                this.logoutHandler.logout(request, response, authentication);
                this.logoutHandler.setClearAuthentication(true);
            }else{
                throw new LogoutException("JWT not found in repo");
            }
            
        }else{
            throw new LogoutException("Error parsing jwt from request");
        } 
   }
}
