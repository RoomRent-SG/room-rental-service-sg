package com.thiha.roomrent.auth;


import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thiha.roomrent.dto.TokenDto;
import com.thiha.roomrent.security.RoomRentUserDetailsService;
import com.thiha.roomrent.service.JwtTokenService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtAuthorizationFilter extends OncePerRequestFilter{
    private final JwtUtils jwtUtils;
    private final ObjectMapper mapper;
    private final RoomRentUserDetailsService roomRentUserDetailsService;
    private final JwtTokenService tokenService;

    public JwtAuthorizationFilter(JwtUtils jwtUtils, ObjectMapper mapper,
     RoomRentUserDetailsService roomRentUserDetailsService,
                                  JwtTokenService tokenService) {
        this.jwtUtils = jwtUtils;
        this.mapper = mapper;
        this.roomRentUserDetailsService = roomRentUserDetailsService;
        this.tokenService = tokenService;
    }

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws StreamWriteException, DatabindException, IOException, ServletException{
            Map<String, Object> errorDetails = new HashMap<>();

            try{
                String token = jwtUtils.extractTokenFromRequest(request);
                if(token==null){
                    filterChain.doFilter(request, response);
                    return;
                }
                Claims claims = jwtUtils.resolveClaims(request);
                
                /*
                 * Implement logout logic here by checking isRevoked attribute of token 
                 */
                TokenDto tokenDto = tokenService.getTokenUsingTokenValue(token);
                if(claims != null && !jwtUtils.isJwtTokenExpired(claims) && tokenDto!=null && !tokenDto.isRevoked()){
                    String userID = claims.getSubject();
                    System.out.println("userID "+userID);
                    UserDetails userDetails = roomRentUserDetailsService.loadUserWithUserID(Long.valueOf(userID));
                    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,"", userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }catch(Exception e){
                errorDetails.put("message", "Authentication Error");
                errorDetails.put("details", e.getMessage());
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                mapper.writeValue(response.getWriter(), errorDetails);
            }
            filterChain.doFilter(request, response);
    }
    
}
