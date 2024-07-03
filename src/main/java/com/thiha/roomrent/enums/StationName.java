package com.thiha.roomrent.enums;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonValue;

public enum StationName{
    JURONG_EAST("Jurong East"),
    BUKIT_BATOK("Bukit Batok"),
    BUKIT_GOMBAK("Bukit Gombak"),
    CHOA_CHU_KANG("Choa Chu Kang"),
    YEW_TEE("Yew Tee"),
    KRANJI("Kranji"),
    MARSILING("Marsiling"),
    WOODLANDS("Woodlands"),
    ADMIRALTY("Admiralty"),
    SEMBAWANG("Sembawang"),
    CANBERRA("Canberra"),
    YISHUN("Yishun"),
    KHATIB("Khatib"),
    YIO_CHU_KANG("Yio Chu Kang"),
    ANG_MO_KIO("Ang Mo Kio"),
    BISHAN("Bishan"),
    BRADDELL("Braddell"),
    TOA_PAYOH("Toa Payoh"),
    NOVENA("Novena"),
    NEWTON("Newton"),
    ORCHARD("Orchard"),
    SOMERSET("Somerset"),
    DHOBY_GHAUT("Dhoby Ghaut"),
    CITY_HALL("City Hall"),
    RAFFLES_PLACE("Raffles Place"),
    MARINA_BAY("Marina Bay"),
    MARINA_SOUTH_PIER("Marina South Pier"),
    PASIR_RIS("Pasir Ris"),
    TAMPINES("Tampines"),
    SIMEI("Simei"),
    TANAH_MERAH("Tanah Merah"),
    BEDOK("Bedok"),
    KEMBANGAN("Kembangan"),
    EUNOS("Eunos"),
    PAYA_LEBAR("Paya Lebar"),
    ALJUNIED("Aljunied"),
    KALLANG("Kallang"),
    LAVENDER("Lavender"),
    BUGIS("Bugis"),
    TANJONG_PAGAR("Tanjong Pagar"),
    OUTRAM_PARK("Outram Park"),
    TIONG_BAHRU("Tiong Bahru"),
    REDHILL("Redhill"),
    QUEENSTOWN("Queenstown"),
    COMMONWEALTH("Commonwealth"),
    BUONA_VISTA("Buona Vista"),
    DOVER("Dover"),
    CLEMENTI("Clementi"),
    CHINESE_GARDEN("Chinese Garden"),
    LAKESIDE("Lakeside"),
    BOON_LAY("Boon Lay"),
    PIONEER("Pioneer"),
    JOO_KOON("Joo Koon"),
    GUL_CIRCLE("Gul Circle"),
    TUAS_CRESCENT("Tuas Crescent"),
    TUAS_WEST_ROAD("Tuas West Road"),
    TUAS_LINK("Tuas Link"),
    EXPO("Expo"),
    CHANGI_AIRPORT("Changi Airport"),
    HARBOURFRONT("HarbourFront"),
    CHINATOWN("Chinatown"),
    CLARKE_QUAY("Clarke Quay"),
    LITTLE_INDIA("Little India"),
    FARRER_PARK("Farrer Park"),
    BOON_KENG("Boon Keng"),
    POTONG_PASIR("Potong Pasir"),
    WOODLEIGH("Woodleigh"),
    SERANGOON("Serangoon"),
    KOVAN("Kovan"),
    HOUGANG("Hougang"),
    BUANGKOK("Buangkok"),
    SENGKANG("Sengkang"),
    PUNGGOL("Punggol"),
    BRAS_BASAH("Bras Basah"),
    ESPLANADE("Esplanade"),
    PROMENADE("Promenade"),
    NICOLL_HIGHWAY("Nicoll Highway"),
    STADIUM("Stadium"),
    MOUNTBATTEN("Mountbatten"),
    DAKOTA("Dakota"),
    MACPHERSON("MacPherson"),
    TAI_SENG("Tai Seng"),
    BARTLEY("Bartley"),
    LORONG_CHUAN("Lorong Chuan"),
    MARYMOUNT("Marymount"),
    CALDECOTT("Caldecott"),
    BOTANIC_GARDENS("Botanic Gardens"),
    FARRER_ROAD("Farrer Road"),
    HOLLAND_VILLAGE("Holland Village"),
    ONE_NORTH("one-north"),
    KENT_RIDGE("Kent Ridge"),
    HAW_PAR_VILLA("Haw Par Villa"),
    PASIR_PANJANG("Pasir Panjang"),
    LABRADOR_PARK("Labrador Park"),
    TELOK_BLANGAH("Telok Blangah"),
    BAYFRONT("Bayfront"),
    BUKIT_PANJANG("Bukit Panjang"),
    CASHEW("Cashew"),
    HILLVIEW("Hillview"),
    BEAUTY_WORLD("Beauty World"),
    KING_ALBERT_PARK("King Albert Park"),
    SIXTH_AVENUE("Sixth Avenue"),
    TAN_KAH_KEE("Tan Kah Kee"),
    STEVENS("Stevens"),
    ROCHOR("Rochor"),
    DOWNTOWN("Downtown"),
    TELOK_AYER("Telok Ayer"),
    FORT_CANNING("Fort Canning"),
    BENCOOLEN("Bencoolen"),
    JALAN_BESAR("Jalan Besar"),
    BENDEMEER("Bendemeer"),
    GEYLANG_BAHRU("Geylang Bahru"),
    MATTAR("Mattar"),
    UBI("Ubi"),
    KAKI_BUKIT("Kaki Bukit"),
    BEDOK_NORTH("Bedok North"),
    BEDOK_RESERVOIR("Bedok Reservoir"),
    TAMPINES_WEST("Tampines West"),
    TAMPINES_EAST("Tampines East"),
    UPPER_CHANGI("Upper Changi"),
    WOODLANDS_NORTH("Woodlands North"),
    WOODLANDS_SOUTH("Woodlands South");

    private final String station;

    private StationName(String stationName){
        this.station = stationName;
    }

    @JsonValue
    public String getStationName(){
        return this.station;
    }

    public static List<String> getValueList(){
     Set<StationName> enums = EnumSet.allOf(StationName.class);
     List<String> valueList = new ArrayList<>();
     for (StationName stationName : enums){
          valueList.add(stationName.getStationName());
     }
     return valueList;
   }
}
