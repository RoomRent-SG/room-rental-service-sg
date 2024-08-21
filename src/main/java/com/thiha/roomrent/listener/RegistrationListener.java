package com.thiha.roomrent.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import com.thiha.roomrent.event.OnRegisterationCompleteEvent;
import com.thiha.roomrent.service.EmailService;

import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;

@Component
public class RegistrationListener implements ApplicationListener<OnRegisterationCompleteEvent> {

    @Autowired
    private EmailService emailService;

    @Override
    @Async
    public void onApplicationEvent(@NonNull OnRegisterationCompleteEvent event) {
        System.out.println("Inside app listener");
        System.out.println(event.getUsername());
        System.out.println(event.getMessage());
        try{
            emailService.sendRegisterationConfirmationEmail(event);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("error sending email");
        }
    }
    
}
