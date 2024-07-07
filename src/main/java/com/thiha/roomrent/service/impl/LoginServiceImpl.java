package com.thiha.roomrent.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.thiha.roomrent.auth.JwtUtils;
import com.thiha.roomrent.dto.LoginRequestDto;
import com.thiha.roomrent.dto.LoginResponseDto;
import com.thiha.roomrent.exceptions.RefreshTokenInvalidException;
import com.thiha.roomrent.model.JwtToken;
import com.thiha.roomrent.model.UserModel;
import com.thiha.roomrent.security.UserDetailsImpl;
import com.thiha.roomrent.service.LoginService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService{
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    JwtTokenService jwtTokenService;
    @Override
    public LoginResponseDto performLogin(LoginRequestDto loginRequestDto,
                                         AuthenticationManager authenticationManager,
                                         HttpServletResponse response) {
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl)authentication.getPrincipal();
            UserModel user = userDetails.getUser();
            log.info("user " + user);
            String userID = user.getId().toString();

            String accessToken = jwtUtils.generateJwtToken(userID, false);
            JwtToken token = new JwtToken();
            token.setToken(accessToken);
            token.setUser(user);
            jwtTokenService.saveToken(token);

            String refreshToken = jwtUtils.generateJwtToken(userID, true);
            Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setSecure(false);
            refreshTokenCookie.setPath("/api/auth/refresh");
            response.addCookie(refreshTokenCookie);
            
            return new LoginResponseDto(user.getId(), accessToken);
        }catch(BadCredentialsException e){
            throw new BadCredentialsException("Invalid password");
        }
        catch(AuthenticationException e){
            log.error(e.getMessage());
            throw new BadCredentialsException("Invalid password");
        }
    }
    @Override
    public LoginResponseDto refreshToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String refreshToken = null;

        if(cookies!=null){
            for(Cookie cookie: cookies){
                if(cookie.getName().equals("refreshToken")){
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if(refreshToken!=null && !jwtUtils.isTokenExpired(refreshToken)){
            String userID = jwtUtils.getIdFromToken(refreshToken);
            String newAccessToken = jwtUtils.generateJwtToken(userID,false);
            return new LoginResponseDto(Long.parseLong(newAccessToken), newAccessToken);
        }
        throw new RefreshTokenInvalidException();
    }    
}
