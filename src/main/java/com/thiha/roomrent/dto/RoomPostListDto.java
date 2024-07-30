package com.thiha.roomrent.dto;

import java.io.Serializable;
import java.util.Date;
import com.thiha.roomrent.enums.Location;
import com.thiha.roomrent.enums.PropertyType;
import com.thiha.roomrent.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomPostListDto implements Serializable {
    private Long id;
    private double price;
    private String thumbnailImage;
    private Location location;
    private RoomType roomType;
    private PropertyType propertyType;
    private String stationName;
    private Date postedAt;
} 
