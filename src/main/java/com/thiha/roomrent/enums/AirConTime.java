package com.thiha.roomrent.enums;

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
}
