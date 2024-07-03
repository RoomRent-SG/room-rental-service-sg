package com.thiha.roomrent.enums;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AirConTime{
   LIMITED("Limited"),
   UNLIMITED("Unlimited");

   private final String airConTime;
   private AirConTime(String airConTime){
        this.airConTime = airConTime;
   }

   @JsonValue
   public String getAirConTime(){
     return this.airConTime;
   }

   public static List<String> getValueList(){
     Set<AirConTime> enums = EnumSet.allOf(AirConTime.class);
     List<String> valueList = new ArrayList<>();
     for (AirConTime ac : enums){
          valueList.add(ac.getAirConTime());
     }
     return valueList;
   }
}
