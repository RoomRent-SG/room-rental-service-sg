package com.thiha.roomrent.enums;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
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

    public static List<String> getValueList(){
     Set<CookingAllowance> enums = EnumSet.allOf(CookingAllowance.class);
     List<String> valueList = new ArrayList<>();
     for (CookingAllowance cooking : enums){
          valueList.add(cooking.getCookingAllowance());
     }
     return valueList;
   }
}
