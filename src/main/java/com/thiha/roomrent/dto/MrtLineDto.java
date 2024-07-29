package com.thiha.roomrent.dto;

import java.util.Set;

import lombok.Data;

@Data
public class MrtLineDto {
    private long id;
    private String lineName;
    private Set<String> stationNames;
}
