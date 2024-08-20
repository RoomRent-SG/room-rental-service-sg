package com.thiha.roomrent.exceptions;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidRegistrationConfirmationTokenException extends RuntimeException{
    public InvalidRegistrationConfirmationTokenException(String message){
        super(message);
    }
}
