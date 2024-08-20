package com.thiha.roomrent.service;

import org.springframework.scheduling.annotation.Async;

import com.thiha.roomrent.event.OnRegisterationCompleteEvent;

public interface EmailService {
    @Async
    void sendRegisterationConfirmationEmail(OnRegisterationCompleteEvent event);
}
