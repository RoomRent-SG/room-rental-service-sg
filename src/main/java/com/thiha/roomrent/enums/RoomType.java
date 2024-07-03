package com.thiha.roomrent.enums;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RoomType {
    COMMON_ROOM("Common Room"),
    MASTER_ROOM("Master Room"),
    STUDY_ROOM("Study Room"),
    STUIO_ROOM("Studio Room");

    private final String roomType;

    private RoomType(String roomType){
        this.roomType = roomType;
    }

    @JsonValue
    public String getRoomType(){
        return this.roomType;
    }

    public static List<String> getValueList(){
     Set<RoomType> enums = EnumSet.allOf(RoomType.class);
     List<String> valueList = new ArrayList<>();
     for (RoomType room : enums){
          valueList.add(room.getRoomType());
     }
     return valueList;
   }
}
