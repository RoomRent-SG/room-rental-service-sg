package com.thiha.roomrent.dto;

import java.lang.reflect.Field;

import com.amazonaws.services.iotsitewise.model.PropertyType;
import com.thiha.roomrent.enums.AirConTime;
import com.thiha.roomrent.enums.CookingAllowance;
import com.thiha.roomrent.enums.Location;
import com.thiha.roomrent.enums.RoomType;
import com.thiha.roomrent.enums.SharePub;
import com.thiha.roomrent.enums.StationName;

import lombok.Data;

/*
 * For creation of object that filters the room posts
 */


 @Data
public class RoomPostSearchFilter {
    private StationName stationName;
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
}

