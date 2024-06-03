package com.thiha.roomrent.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thiha.roomrent.dto.TokenDto;
import com.thiha.roomrent.mapper.TokenMapper;
import com.thiha.roomrent.model.JwtToken;
import com.thiha.roomrent.repository.JwtTokenRepository;
import com.thiha.roomrent.service.impl.JwtTokenService;

@Service
public class JwtTokenServiceImpl implements JwtTokenService{
    @Autowired
    private JwtTokenRepository tokenRepository;

    @Override
    public TokenDto saveToken(JwtToken token) {
        JwtToken savedToken = tokenRepository.save(token);
        return TokenMapper.mapToTokenDto(savedToken);
    }
    
}
