package com.thiha.roomrent.service.impl;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Date;
import com.thiha.roomrent.dto.ConfirmationTokenDto;
import com.thiha.roomrent.exceptions.InvalidRegistrationConfirmationTokenException;
import com.thiha.roomrent.mapper.ConfirmationTokenMapper;
import com.thiha.roomrent.model.ConfirmationToken;
import com.thiha.roomrent.repository.ConfirmationTokenRepository;
import com.thiha.roomrent.service.ConfirmationTokenService;
import com.thiha.roomrent.utility.DateTimeHandler;

@Service
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Override
    public ConfirmationTokenDto getConfirmationToken(UUID tokenValue) {
        Optional<ConfirmationToken> optionalToken = confirmationTokenRepository.findByTokenValue(tokenValue);
        if (optionalToken.isPresent()) {
            return ConfirmationTokenMapper.mapToConfirmationTokenDto(optionalToken.get());
        }
        throw new InvalidRegistrationConfirmationTokenException("Invalid Confirmation Token");
    }

    @Override
    public boolean isTokenValid(ConfirmationTokenDto tokenDto) {
        Date createAt = tokenDto.getCreatedAt();
        Date now = DateTimeHandler.getUTCNow();
        long timeDifference = now.getTime() - createAt.getTime();
        long hoursDifference = timeDifference / (1000 * 60 * 60);
        return hoursDifference <= 42;
    }

}
