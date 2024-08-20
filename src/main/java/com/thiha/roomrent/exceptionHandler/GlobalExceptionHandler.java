package com.thiha.roomrent.exceptionHandler;

import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.thiha.roomrent.dto.ErrorResponse;
import com.thiha.roomrent.exceptions.EmailAlreadyRegisteredException;
import com.thiha.roomrent.exceptions.EntityNotFoundException;
import com.thiha.roomrent.exceptions.InvalidObjectException;
import com.thiha.roomrent.exceptions.InvalidRegistrationConfirmationTokenException;
import com.thiha.roomrent.exceptions.LogoutException;
import com.thiha.roomrent.exceptions.NameAlreadyExistedException;
import com.thiha.roomrent.exceptions.ProfileImageNotFoundException;
import com.thiha.roomrent.exceptions.RefreshTokenInvalidException;
import com.thiha.roomrent.exceptions.RoomPhotoNotFoundException;
import com.thiha.roomrent.exceptions.RoomPhotosExceedLimitException;
import com.thiha.roomrent.exceptions.S3ImageUploadException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    

    @ExceptionHandler(S3ImageUploadException.class)
    public ResponseEntity<ErrorResponse> handleImageUploadException(S3ImageUploadException exception){
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getErrorMessage());
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyRegisteredException(EmailAlreadyRegisteredException exception){
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getErrorMessage());
        return ResponseEntity
                            .badRequest()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(response);
    }

    @ExceptionHandler(NameAlreadyExistedException.class)
    public ResponseEntity<ErrorResponse> handleNameAlredyExistedException(NameAlreadyExistedException exception){
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getErrorMessage());
        return ResponseEntity
                            .badRequest()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException exception){
        ErrorResponse response = new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getErrorMassage());
        return ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(response);
    }

    @ExceptionHandler(ProfileImageNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProfileImageNotFoundException(ProfileImageNotFoundException exception){
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getErrorMessage());
        return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(response);
    }

    @ExceptionHandler(RoomPhotoNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRoomPhotoNotFoundException(RoomPhotoNotFoundException exception){
        ErrorResponse response = new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getErrorMessage());
        return ResponseEntity
                            .badRequest()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(response);
    }


    @ExceptionHandler(RoomPhotosExceedLimitException.class)
    public ResponseEntity<ErrorResponse> handleRoomPhotosExceedLimitException(RoomPhotosExceedLimitException exception){
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getErrorMessage());
        return ResponseEntity
                            .badRequest()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(response);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponse> handleIOException(IOException exception){
        ErrorResponse response = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
        return ResponseEntity
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(response);
    }

    @ExceptionHandler(LogoutException.class)
    public ResponseEntity<ErrorResponse> handleLogoutException(LogoutException exception){
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getErrorMessage());
        return ResponseEntity
                            .badRequest()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialException(BadCredentialsException exception){
        ErrorResponse response = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), exception.getMessage());
        return ResponseEntity
                            .status(HttpStatus.UNAUTHORIZED)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(response);
    }

    @ExceptionHandler(InvalidObjectException.class)
    public ResponseEntity<ErrorResponse> handleInvalidObjectException(InvalidObjectException exception){
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getErrorMessage());
        System.out.println("Exception Handling in global hanlder..."+ exception.getErrorMessage());
        return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(response);
    }

    @ExceptionHandler(RefreshTokenInvalidException.class)
    public ResponseEntity<ErrorResponse> handleRefreshTokenInvalidException(RefreshTokenInvalidException exception){
        ErrorResponse response = new ErrorResponse(HttpStatus.FORBIDDEN.value(), exception.getErrorMessage());
        return ResponseEntity
                            .status(HttpStatus.FORBIDDEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(response);
    }

    @ExceptionHandler(InvalidRegistrationConfirmationTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRegistrationConfirmationTokenException(InvalidRegistrationConfirmationTokenException exception){
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(response);
    }

}
