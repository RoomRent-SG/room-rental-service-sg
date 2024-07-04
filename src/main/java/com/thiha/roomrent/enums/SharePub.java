package com.thiha.roomrent.enums;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

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

    @Override
    public String toString(){
        return this.pub;
    }

    public static List<String> getValueList(){
     Set<SharePub> enums = EnumSet.allOf(SharePub.class);
     List<String> valueList = new ArrayList<>();
     for (SharePub pub : enums){
          valueList.add(pub.getSharePub());
     }
     return valueList;
   }
}
