package com.thiha.roomrent.enums;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Location {
    RAFFLES_PLACE("Raffles Place"),
    MARINA("Marina"),
    CECIL("Cecil"),
    JOO_CHIAT("Joo Chiat"),
    AMBER_ROAD("Amber Road"),
    KATONG("Katong"),
    ANSON("Anson"),
    TANJONG_PAGAR("Tanjong Pagar"),
    BEDOK("Bedok"),
    UPPER_EAST_COAST("Upper East Coast"),
    EASTWOOD("Eastwood"),
    KEW_DRIVE("Kew Drive"),
    TIONG_BAHRU("Tiong Bahru"),
    QUEENSTOWN("Queenstown"),
    CHANGI("Changi"),
    LOYANG("Loyang"),
    TELOK_BLANGAH("Telok Blangah"),
    HARBOURFRONT("Harbourfront"),
    TAMPINES("Tampines"),
    PASIR_RIS("Pasir Ris"),
    PASIR_PANJANG("Pasir Panjang"),
    HONG_LEONG_GARDEN("Hong Leong Garden"),
    CLEMENTI_NEW_TOWN("Clementi New Town"),
    PUNGGOL("Punggol"),
    HOUGANG("Hougang"),
    SERANGOON_GARDENS("Serangoon Gardens"),
    HIGH_STREET("High Street"),
    BEACH_ROAD("Beach Road"),
    ANG_MO_KIO("Ang Mo Kio"),
    BISHAN("Bishan"),
    MIDDLE_ROAD("Middle Road"),
    GOLDEN_MILE("Golden Mile"),
    UPPER_BUKIT_TIMAH("Upper Bukit Timah"),
    ULU_PANDAN("Ulu Pandan"),
    CLEMENTI_PARK("Clementi Park"),
    LITTLE_INDIA("Little India"),
    JURONG_EAST("Jurong East"),
    JURONG_WEST("Jurong West"),
    ORCHARD("Orchard"),
    CAIRNHILL("Cairnhill"),
    RIVER_VALLEY("River Valley"),
    CHOA_CHU_KANG("Choa Chu Kang"),
    DAIRY_FARM("Dairy Farm"),
    HILLVIEW("Hillview"),
    BUKIT_PANJANG("Bukit Panjang"),
    ARDMORE("Ardmore"),
    BUKIT_TIMAH("Bukit Timah"),
    HOLLAND_ROAD("Holland Road"),
    TANGLIN("Tanglin"),
    LIM_CHU_KANG("Lim Chu Kang"),
    TENGAH("Tengah"),
    WATTEN_ESTATE("Watten Estate"),
    NOVENA("Novena"),
    THOMSON("Thomson"),
    KRANJI("Kranji"),
    WOODGROVE("Woodgrove"),
    TOA_PAYOH("Toa Payoh"),
    SERANGOON("Serangoon"),
    BALESTIER("Balestier"),
    UPPER_THOMSON("Upper Thomson"),
    SPRINGLEAF("Springleaf"),
    MACPHERSON("Macpherson"),
    BRADELL("Bradell"),
    SEMBAWANG("Sembawang"),
    YISHUN("Yishun"),
    GEYLANG("Geylang"),
    EUNOS("Eunos"),
    SELETAR("Seletar");

    private final String location;

    Location(String location) {
        this.location= location;
    }

    @JsonValue
    public String getLocation(){
        return this.location;
    }
    
    public static List<String> getValueList(){
     Set<Location> enums = EnumSet.allOf(Location.class);
     List<String> valueList = new ArrayList<>();
     for (Location location : enums){
          valueList.add(location.getLocation());
     }
     return valueList;
   }
}
