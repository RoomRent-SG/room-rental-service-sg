package com.thiha.roomrent.service;

import java.util.UUID;
import com.thiha.roomrent.dto.ConfirmationTokenDto;

public interface ConfirmationTokenService {
    ConfirmationTokenDto getConfirmationToken(UUID tokenValue);
    boolean isTokenValid(ConfirmationTokenDto tokenDto);
}
