package com.thiha.roomrent.dto;


import java.util.List;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RoomPostRegisterDto {
   
    @NotNull(message = "Station name cannot be null")
    @NotEmpty(message = "Station name cannot be empty")
    private String stationName;

    @NotNull(message = "Address cannot be null")
    private String address;
    /*
     * need to validate somehow
     */
    @Range(min = 300, message = "Price is not realistic")
    private double price;

    @NotBlank(message = "Room Type cannot be null")
    private String roomType;

    @Min(value = 1, message = "Total person must be greater than 0")
    private int totalPax;

    @NotBlank(message = "Cooking Allowance cannot be empty")
    private String cookingAllowance;

    @NotBlank(message = "Pub Share field cannot be null")
    private String sharePub;

    @NotBlank(message = "AirconTime field cannot be null")
    private String airConTime;

    private boolean allowVisitor;

    @NotBlank(message = "Location cannot be null")
    private String location;

    @NotBlank(message = "Property type cannot be empty")
    private String propertyType;

    private List<MultipartFile> roomPhotoFiles;

    @NotBlank(message = "Description cannot be null")
    private String description;

}
