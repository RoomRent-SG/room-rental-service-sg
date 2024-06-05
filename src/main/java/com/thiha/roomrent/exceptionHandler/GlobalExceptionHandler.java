package com.thiha.roomrent.exceptionHandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.thiha.roomrent.exceptions.EmailAlreadyRegisteredException;
import com.thiha.roomrent.exceptions.NameAlreadyExistedException;
import com.thiha.roomrent.exceptions.S3ImageUploadException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    

    @ExceptionHandler(S3ImageUploadException.class)
    public ResponseEntity<?> handleImageUploadException(S3ImageUploadException exception){
        return ResponseEntity.internalServerError()
                .body(exception.getMessage());
    }

    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    public ResponseEntity<?> handleEmailAlreadyRegisteredException(EmailAlreadyRegisteredException exception){
        return ResponseEntity
                            .badRequest()
                            .body(exception.getErrorMessage());
    }

    @ExceptionHandler(NameAlreadyExistedException.class)
    public ResponseEntity<?> handleNameAlredyExistedException(NameAlreadyExistedException exception){
        return ResponseEntity
                            .badRequest()
                            .body(exception.getErrorMessage());
    }
}
