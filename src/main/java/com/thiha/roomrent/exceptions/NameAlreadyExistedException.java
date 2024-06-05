package com.thiha.roomrent.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NameAlreadyExistedException extends RuntimeException{
    private String errorMessage;
}
