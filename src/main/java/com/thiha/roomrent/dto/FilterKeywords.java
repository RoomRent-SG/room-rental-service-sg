package com.thiha.roomrent.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thiha.roomrent.enums.AirConTime;
import com.thiha.roomrent.enums.CookingAllowance;
import com.thiha.roomrent.enums.Location;
import com.thiha.roomrent.enums.PropertyType;
import com.thiha.roomrent.enums.RoomType;
import com.thiha.roomrent.enums.SharePub;
import com.thiha.roomrent.enums.StationName;

import lombok.Data;

@Data
public class FilterKeywords {
    private Map<String, List<String>> searchParams;

     public FilterKeywords(){
      searchParams = new HashMap<>();
      searchParams.put("airConTime", AirConTime.getValueList());
      searchParams.put("cookingAllowance", CookingAllowance.getValueList());
      searchParams.put("location", Location.getValueList());
      searchParams.put("propertyType", PropertyType.getValueList());
      searchParams.put("roomType", RoomType.getValueList());
      searchParams.put("sharePub", SharePub.getValueList());
      searchParams.put("stationName", StationName.getValueList());
   }
}
