package com.thiha.roomrent.enums;

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
}
