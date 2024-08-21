package com.thiha.roomrent.mapper;

import com.thiha.roomrent.dto.ConfirmationTokenDto;
import com.thiha.roomrent.model.ConfirmationToken;

public class ConfirmationTokenMapper {
    public static ConfirmationTokenDto mapToConfirmationTokenDto(ConfirmationToken token){
        ConfirmationTokenDto dto = new ConfirmationTokenDto();
        dto.setId(token.getId());
        dto.setToken(token.getTokenValue());
        dto.setUserId(token.getAgent().getId());
        dto.setCreatedAt(token.getCreatedAt());
        return dto;
    }
}
