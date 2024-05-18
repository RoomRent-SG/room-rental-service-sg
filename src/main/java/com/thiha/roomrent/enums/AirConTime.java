package com.thiha.roomrent.enums;

public enum AirConTime {
   LIMITED("limited"),
   UNLIMITED("unlimited");

   private final String airConTime;
   private AirConTime(String airConTime){
        this.airConTime = airConTime;
   }

   @Override
   public String toString(){
        return this.airConTime;
   }
}
