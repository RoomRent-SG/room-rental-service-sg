package com.thiha.roomrent.enums;

public enum UserRole {
    ADMIN("admin"),
    AGENT("agent");

    private String role;
    private UserRole(String role){
        this.role = role;
    }

    @Override
    public String toString(){
        return this.role;
    }
}
