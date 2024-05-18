package com.thiha.roomrent.enums;

public enum SharePub {
    INCLUSIVE("PUB inclusive"),
    SHARE("share PUB");

    private final String pub;
    private SharePub(String pub){
        this.pub = pub;
    }

    @Override
    public String toString(){
        return this.pub;
    }
}
