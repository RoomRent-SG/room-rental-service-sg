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
    private StationName stationName;
    private double price;
    @JsonIgnore
    private Date postedAt;
    private RoomType roomType;
    private int totalPax;
    private CookingAllowance cookingAllowance;
    private SharePub sharePub;
    private AirConTime airConTime;
    private boolean allowVisitor;
    private Location location;
    private PropertyType propertyType;
    private List<MultipartFile> roomPhotoFiles;
    @JsonIgnore
    private Agent agent;
    @JsonIgnore
    private List<RoomPhoto> roomPhotos;
}
