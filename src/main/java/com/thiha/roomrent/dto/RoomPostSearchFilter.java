package com.thiha.roomrent.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thiha.roomrent.enums.AirConTime;
import com.thiha.roomrent.enums.CookingAllowance;
import com.thiha.roomrent.enums.Location;
import com.thiha.roomrent.enums.PropertyType;
import com.thiha.roomrent.enums.RoomType;
import com.thiha.roomrent.enums.SharePub;
import lombok.Data;

/*
 * For creation of object that filters the room posts
 */


 @Data
public class RoomPostSearchFilter {
    private String stationName;
    private Location location;
    private Double minPrice;
    private Double maxPrice;
    private RoomType roomType;
    private Integer totalPax;
    private CookingAllowance cookingAllowance;
    private SharePub sharePub;
    private AirConTime airConTime;
    private boolean allow_visitor;
    private PropertyType propertyType;
    @JsonIgnore
    private boolean isArchived;
}

