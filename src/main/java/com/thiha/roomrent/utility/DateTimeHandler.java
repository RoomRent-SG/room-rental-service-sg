package com.thiha.roomrent.utility;

import java.time.Instant;
import java.util.Date;

public class DateTimeHandler {
    
    public static Date getUTCNow(){
        Instant instant = Instant.now();
        
        return Date.from(instant);
    }
}
