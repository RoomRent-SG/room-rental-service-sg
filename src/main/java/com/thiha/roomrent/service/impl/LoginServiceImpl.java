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
import com.thiha.roomrent.model.JwtToken;
import com.thiha.roomrent.model.UserModel;
import com.thiha.roomrent.security.UserDetailsImpl;
import com.thiha.roomrent.service.LoginService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService{
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    JwtTokenService jwtTokenService;
    @Override
    public LoginResponseDto performLogin(LoginRequestDto loginRequestDto, AuthenticationManager authenticationManager) {
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl)authentication.getPrincipal();
            UserModel user = userDetails.getUser();
            log.info("user " + user);
            String jwtToken = jwtUtils.generateJwtToken(loginRequestDto);
            JwtToken token = new JwtToken();
            token.setToken(jwtToken);
            token.setUser(user);
            jwtTokenService.saveToken(token);
            return new LoginResponseDto(loginRequestDto.getUsername(), jwtToken);
        }catch(BadCredentialsException e){
            throw new BadCredentialsException("Invalid password");
        }
        catch(AuthenticationException e){
            log.error(e.getMessage());
            throw new BadCredentialsException("Invalid password");
        }
    }    
}
