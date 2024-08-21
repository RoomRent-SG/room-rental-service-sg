package com.thiha.roomrent.event;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OnRegisterationCompleteEvent extends ApplicationEvent{
    private String message;
    private String username;

    public OnRegisterationCompleteEvent(Object source, String username, String message) {
        super(source);
        this.username = username;
        this.message = message;
    }

    
}
