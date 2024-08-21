package com.thiha.roomrent.dto;

import java.util.UUID;

import lombok.Data;

import java.util.Date;

@Data
public class ConfirmationTokenDto {
    private Long id;
    private UUID token;
    private Date createdAt;
    private Long userId;
}
