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
import com.thiha.roomrent.exceptions.UserDisabledException;
import com.thiha.roomrent.model.JwtToken;
import com.thiha.roomrent.model.UserModel;
import com.thiha.roomrent.security.UserDetailsImpl;
import com.thiha.roomrent.service.JwtTokenService;
import com.thiha.roomrent.service.LoginService;
import com.thiha.roomrent.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Service
public class LoginServiceImpl implements LoginService{
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    JwtTokenService jwtTokenService;
    @Autowired
    UserService userService;

    @Override
    public LoginResponseDto performLogin(LoginRequestDto loginRequestDto,
                                         AuthenticationManager authenticationManager,
                                         HttpServletResponse response) {
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl)authentication.getPrincipal();
            UserModel user = userDetails.getUser();

            if(!user.isEnabled()){
                throw new UserDisabledException("User is currently disabled");
            }

            String accessToken = jwtUtils.generateJwtToken(user, false);
            JwtToken token = new JwtToken();
            token.setToken(accessToken);
            token.setUser(user);
            jwtTokenService.saveToken(token);

            String refreshToken = jwtUtils.generateJwtToken(user, true);
            Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setSecure(false);
            refreshTokenCookie.setPath("/");
            response.addCookie(refreshTokenCookie);
            
            return new LoginResponseDto(user.getId(), accessToken);
        }catch(BadCredentialsException e){
            throw new BadCredentialsException("Invalid password");
        }
        catch(AuthenticationException e){
            throw new BadCredentialsException(e.getMessage());
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
                    // TODO to genearete new refresh token
                    break;
                }
            }
        }

        if(refreshToken!=null && !jwtUtils.isTokenExpired(refreshToken)){
            long userID = Long.valueOf(jwtUtils.getIdFromToken(refreshToken));
            UserModel user = userService.getUser(userID);
            String newAccessToken = jwtUtils.generateJwtToken(user,false);
            return new LoginResponseDto(userID, newAccessToken);
        }
        throw new RefreshTokenInvalidException();
    }    
}
