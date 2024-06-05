package com.thiha.roomrent.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class S3ImageUploadException extends RuntimeException{
    private String errorMessage;
}
