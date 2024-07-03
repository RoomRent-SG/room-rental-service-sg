package com.thiha.roomrent.enums;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PropertyType {
    CONDO("condominium"),
    HDB("HDB");

    private final String propertyType;
    private PropertyType(String propertyType){
        this.propertyType = propertyType;
    }

    @JsonValue
    public String getPropertyType(){
        return this.propertyType;
    }

    public static List<String> getValueList(){
     Set<PropertyType> enums = EnumSet.allOf(PropertyType.class);
     List<String> valueList = new ArrayList<>();
     for (PropertyType property : enums){
          valueList.add(property.getPropertyType());
     }
     return valueList;
   }
}
