package com.thiha.roomrent.dto;

import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thiha.roomrent.enums.AirConTime;
import com.thiha.roomrent.enums.CookingAllowance;
import com.thiha.roomrent.enums.Location;
import com.thiha.roomrent.enums.PropertyType;
import com.thiha.roomrent.enums.RoomType;
import com.thiha.roomrent.enums.SharePub;
import com.thiha.roomrent.enums.StationName;
import com.thiha.roomrent.model.Agent;
import com.thiha.roomrent.model.RoomPhoto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RoomPostRegisterDto {
    @JsonIgnore
    private Long id;
    @NotNull(message = "Station name cannot be null")
    @NotEmpty(message = "Station name cannot be empty")
    private StationName stationName;
    @NotNull(message = "Price cannot be null")
    @NotEmpty(message = "Price cannot be empty")
    private double price;
    @JsonIgnore
    private Date postedAt;
    @NotNull(message = "Room Type cannot be null")
    @NotEmpty(message = "Room Type cannot be empty")
    private RoomType roomType;
    @NotNull(message = "Total person cannot be null")
    @NotEmpty(message = "Total person cannot be empty")
    private int totalPax;
    @NotNull(message = "Cooking Allowance cannot be null")
    @NotEmpty(message = "Cooking Allowance cannot be empty")
    private CookingAllowance cookingAllowance;
    @NotNull(message = "Pub Share field cannot be null")
    @NotEmpty(message = "Pub Share filed cannot be empty")
    private SharePub sharePub;
    @NotNull(message = "AirconTime field cannot be null")
    @NotEmpty(message = "AirconTime field cannot be empty")
    private AirConTime airConTime;
    @NotNull(message = "AllowVisitor field cannot be null")
    @NotEmpty(message = "AllowVisitor field cannot be empty")
    private boolean allowVisitor;
    @NotNull(message = "Location cannot be null")
    @NotEmpty(message = "Location cannot be empty")
    private Location location;
    @NotNull(message = "Property type cannot be null")
    @NotEmpty(message = "Property type cannot be empty")  
    private PropertyType propertyType;
    private List<MultipartFile> roomPhotoFiles;
    @NotNull(message = "Description cannot be null")
    @NotEmpty(message = "Description cannot be empty")
    private String description;
    @JsonIgnore
    private Agent agent;
    @JsonIgnore
    private List<RoomPhoto> roomPhotos;
}
