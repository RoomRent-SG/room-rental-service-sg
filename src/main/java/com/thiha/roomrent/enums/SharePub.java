package com.thiha.roomrent.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SharePub {
    INCLUSIVE("PUB inclusive"),
    SHARE("share PUB");

    private final String pub;
    private SharePub(String pub){
        this.pub = pub;
    }

    @JsonValue
    public String getSharePub(){
        return this.pub;
    }
}
