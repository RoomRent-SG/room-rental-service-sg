package com.thiha.roomrent.enums;

public enum PropertyType {
    CONDO("condominium"),
    HDB("HDB");

    private final String propertyType;
    private PropertyType(String propertyType){
        this.propertyType = propertyType;
    }

    @Override
    public String toString(){
        return this.propertyType;
    }
}
