package com.thiha.roomrent.enums;

public enum PassType {
    VP("Visit Pass"),
    WP("Work Permit"),
    SP("Skill Worker Pass"),
    EP("Employment Pass");

    private final String passType;
    private PassType(String passType){
        this.passType = passType;
    }

    @Override
    public String toString(){
        return this.passType;
    }
}
