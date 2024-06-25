package com.thiha.roomrent.enums;

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
}
