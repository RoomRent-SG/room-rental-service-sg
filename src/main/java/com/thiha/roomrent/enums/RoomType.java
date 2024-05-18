package com.thiha.roomrent.enums;

public enum RoomType {
    COMMON_ROOM("Common Room"),
    MASTER_ROOM("Master Room"),
    STUDY_ROOM("Study Room"),
    STUIO_ROOM("Studio Room");

    private final String roomType;

    private RoomType(String roomType){
        this.roomType = roomType;
    }

    @Override
    public String toString(){
        return this.roomType;
    }
}
