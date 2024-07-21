package com.thiha.roomrent.dto;

import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thiha.roomrent.enums.StationName;
import com.thiha.roomrent.model.Agent;
import com.thiha.roomrent.model.RoomPhoto;
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
    @JsonIgnore
    private Long id;

    @NotNull(message = "Station name cannot be null")
    private String stationName;

    @NotNull(message = "Address cannot be null")
    private String address;
    /*
     * need to validate somehow
     */
    @Range(min = 300, message = "Price is not realistic")
    private double price;
    @JsonIgnore
    private Date postedAt;

    @NotNull(message = "Room Type cannot be null")
    private String roomType;

    @NotNull(message = "Total person cannot be null")
    private int totalPax;

    @NotNull(message = "Cooking Allowance cannot be null")
    private String cookingAllowance;

    @NotNull(message = "Pub Share field cannot be null")
    private String sharePub;

    @NotNull(message = "AirconTime field cannot be null")
    private String airConTime;

    @NotNull(message = "AllowVisitor field cannot be null")
    private boolean allowVisitor;

    @NotNull(message = "Location cannot be null")
    private String location;

    @NotNull(message = "Property type cannot be null") 
    private String propertyType;

    private List<MultipartFile> roomPhotoFiles;

    @NotNull(message = "Description cannot be null")
    private String description;

    @JsonIgnore
    private Agent agent;
    
    @JsonIgnore
    private List<RoomPhoto> roomPhotos;
}
