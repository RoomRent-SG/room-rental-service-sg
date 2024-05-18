package com.thiha.roomrent.auth;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.thiha.roomrent.constant.JwtConstants;
import com.thiha.roomrent.dto.LoginRequestDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtils {
    private final SecretKey signingKey;


    public JwtUtils(){
        this.signingKey = getSigningKey();
    }

    private SecretKey getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(JwtConstants.SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateJwtToken(LoginRequestDto user){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JwtConstants.TOKEN_VALIDITY);
        return Jwts
                    .builder()
                    .subject((user.getUsername()))
                    .issuedAt(new Date())
                    .expiration(expiryDate)
                    .signWith(getSigningKey(), Jwts.SIG.HS256)
                    .compact();
    }

    private Claims parseClaimsFromJwtToken(String token){
        return Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
    }

    public Claims resolveClaims(HttpServletRequest request){
        try{
            String token = extractTokenFromRequest(request);
            if (token != null) {
                return parseClaimsFromJwtToken(token);
            }
            return null;
        }catch(ExpiredJwtException exception){
            request.setAttribute("Token Expired", exception.getMessage());
            throw exception;
        }catch(Exception exception){
            request.setAttribute("Invalid Token", exception.getMessage());
            throw exception;
        }

    }

    

    public String extractTokenFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader(JwtConstants.TOKEN_HEADER);
        if(bearerToken !=null && bearerToken.startsWith(JwtConstants.TOKEN_PREFIX)){
            return bearerToken.substring(JwtConstants.TOKEN_PREFIX.length());
        }
        return null;
    }

    public boolean validateToken(String token){
        try{
            Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public boolean validateClaims(Claims claims)throws AuthenticationException{
        try{
            return claims.getExpiration().after(new Date());
        }catch(Exception e){
            throw e;
        }
    }

    public String getUsernameFromToken(String token){
        Claims claims = parseClaimsFromJwtToken(token);
        return claims.getSubject();
    }

    public boolean isTokenExpired(String token){
        Claims claims = parseClaimsFromJwtToken(token);
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }
    
}
