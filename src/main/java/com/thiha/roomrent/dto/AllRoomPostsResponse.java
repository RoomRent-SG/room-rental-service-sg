package com.thiha.roomrent.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thiha.roomrent.enums.AirConTime;
import com.thiha.roomrent.enums.CookingAllowance;
import com.thiha.roomrent.enums.Location;
import com.thiha.roomrent.enums.PropertyType;
import com.thiha.roomrent.enums.RoomType;
import com.thiha.roomrent.enums.SharePub;
import com.thiha.roomrent.enums.StationName;

import lombok.Data;

@Data
public class AllRoomPostsResponse implements Serializable{
   private List<RoomPostListDto> allRoomPosts;
   private int pageNo;
   private int pageSize;
   private Long totalContentSize;
   private boolean last;
}
