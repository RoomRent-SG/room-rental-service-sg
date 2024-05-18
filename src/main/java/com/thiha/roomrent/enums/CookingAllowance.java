package com.thiha.roomrent.enums;

public enum CookingAllowance {
    NO_COOKING("No Cooking Allowed"),
    LIGHT_COOKING("Light Cooking Allowed"),
    COOKING_ALLOWED("Cooking Allowed");

    private final String cooking;

    private CookingAllowance(String cooking){
        this.cooking = cooking;
    }

    @Override
    public String toString(){
        return this.cooking;
    }
}
