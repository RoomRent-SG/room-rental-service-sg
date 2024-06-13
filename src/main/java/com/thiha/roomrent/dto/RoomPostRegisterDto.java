package com.thiha.roomrent.dto;

import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Range;
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
    private StationName stationName;
    /*
     * need to validate somehow
     */
    @Range(min = 300, message = "Price is not realistic")
    private double price;
    @JsonIgnore
    private Date postedAt;

    @NotNull(message = "Room Type cannot be null")
    private RoomType roomType;

    @NotNull(message = "Total person cannot be null")
    private int totalPax;

    @NotNull(message = "Cooking Allowance cannot be null")
    private CookingAllowance cookingAllowance;

    @NotNull(message = "Pub Share field cannot be null")
    private SharePub sharePub;

    @NotNull(message = "AirconTime field cannot be null")
    private AirConTime airConTime;

    @NotNull(message = "AllowVisitor field cannot be null")
    private boolean allowVisitor;

    @NotNull(message = "Location cannot be null")
    private Location location;

    @NotNull(message = "Property type cannot be null") 
    private PropertyType propertyType;

    private List<MultipartFile> roomPhotoFiles;

    @NotNull(message = "Description cannot be null")
    private String description;

    @JsonIgnore
    private Agent agent;
    
    @JsonIgnore
    private List<RoomPhoto> roomPhotos;
}
