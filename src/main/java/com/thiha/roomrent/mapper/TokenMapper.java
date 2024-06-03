package com.thiha.roomrent.mapper;

import com.thiha.roomrent.dto.TokenDto;
import com.thiha.roomrent.model.JwtToken;

public class TokenMapper {
   public static JwtToken mapToJwtToken(TokenDto tokenDto){
        return new JwtToken(
            tokenDto.getId(),
            tokenDto.getToken(),
            tokenDto.isRevoked(),
            tokenDto.getUser()
        );
   }

   public static TokenDto mapToTokenDto(JwtToken token){
        return new TokenDto(
            token.getId(),
            token.getToken(),
            token.isRevoked(),
            token.getUser()
        );
   }
}
