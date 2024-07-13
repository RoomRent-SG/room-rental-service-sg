package com.thiha.roomrent.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import com.thiha.roomrent.auth.JwtUtils;
import com.thiha.roomrent.dto.TokenDto;
import com.thiha.roomrent.exceptions.LogoutException;
import com.thiha.roomrent.mapper.TokenMapper;
import com.thiha.roomrent.service.impl.JwtTokenServiceImpl;

import jakarta.servlet.http.Cookie;
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
        System.out.println("Token "+ stringToken);
        if(stringToken!=null){
            TokenDto token = tokenService.getTokenUsingTokenValue(stringToken);
            
            if(token !=null){
                token.setRevoked(true);
                tokenService.saveToken(TokenMapper.mapToJwtToken(token));
                Cookie cookie = getRefreshTokenCookieFromRequest(request);
                cookie.setValue(null);
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
                // clean up 
                this.logoutHandler.logout(request, response, authentication);
                this.logoutHandler.setClearAuthentication(true);
            }else{
                System.out.println("Extracted Token is null.....");
                throw new LogoutException("JWT not found in repo");
            }
            
        }else{
            System.out.println("DB Token is null...");
            throw new LogoutException("Error parsing jwt from request");
        } 
   }

   private Cookie getRefreshTokenCookieFromRequest(HttpServletRequest request){
    Cookie[] cookies = request.getCookies();
    if(cookies != null){
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("refreshToken")){
                return cookie;
            }
        }
    }
    System.out.println("Cookie notfound..");
    return new Cookie("refreshToken", null);

   }
}
