package com.thiha.roomrent.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CookingAllowance {
    NO_COOKING("No Cooking Allowed"),
    LIGHT_COOKING("Light Cooking Allowed"),
    COOKING_ALLOWED("Cooking Allowed");

    private final String cooking;

    private CookingAllowance(String cooking){
        this.cooking = cooking;
    }

    @JsonValue
    public String getCookingAllowance(){
        return this.cooking;
    }
}
