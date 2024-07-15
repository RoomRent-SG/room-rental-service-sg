package com.thiha.roomrent.auth;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import com.thiha.roomrent.constant.JwtConstants;
import com.thiha.roomrent.model.UserModel;
import com.thiha.roomrent.utility.DateTimeHandler;

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

    public String generateJwtToken(UserModel user, Boolean isRefreshToken){
        Date now = DateTimeHandler.getUTCNow();
        long tokenValidity;
        if(isRefreshToken){
            tokenValidity = JwtConstants.TOKEN_VALIDITY;
        }else{
            tokenValidity = JwtConstants.REFRESH_TOKEN_VALIDITY;
        }
        Date expiryDate = new Date(now.getTime() + tokenValidity);
        return Jwts
                    .builder()
                    .subject(user.getId().toString())
                    .issuedAt(DateTimeHandler.getUTCNow())
                    .expiration(expiryDate)
                    .signWith(getSigningKey(), Jwts.SIG.HS256)
                    .claim("user", createJwtClaims(user))
                    .compact();
    }

    private Map<String, Object> createJwtClaims(UserModel user){
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("role", user.getRole());
        return claims;
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

    // checked the token expiration
    public boolean isJwtTokenExpired(Claims claims)throws AuthenticationException{
        try{
            return claims.getExpiration().before(DateTimeHandler.getUTCNow());
        }catch(Exception e){
            throw e;
        }
    }

    public String getIdFromToken(String token){
        Claims claims = parseClaimsFromJwtToken(token);
        return claims.getSubject();
    }

    public boolean isTokenExpired(String token){
        Claims claims = parseClaimsFromJwtToken(token);
        Date expiration = claims.getExpiration();
        return expiration.before(DateTimeHandler.getUTCNow());
    }
    
}
