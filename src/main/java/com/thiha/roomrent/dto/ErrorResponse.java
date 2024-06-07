package com.thiha.roomrent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
   private Integer statusCode;
   private String errorMessage;
}
