package com.thiha.roomrent.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.thiha.roomrent.event.OnRegisterationCompleteEvent;
import com.thiha.roomrent.model.Agent;
import com.thiha.roomrent.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService{
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendRegisterationConfirmationEmail(OnRegisterationCompleteEvent event) {
        // TODO check type
        Agent registeredAgent = (Agent) event.getSource();
        String apiEndpoint = "http://localhost:8080/complete-registration";

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(registeredAgent.getEmail());
        mail.setSubject("Complete Registration");
        mail.setText(apiEndpoint+"?token="+registeredAgent.getConfirmationToken().getTokenValue());
        mailSender.send(mail);
    }
    
}
