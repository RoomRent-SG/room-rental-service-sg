package com.thiha.roomrent.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class AllRoomPostsResponse implements Serializable{
   private List<RoomPostListDto> allRoomPosts;
   private int pageNo;
   private int pageSize;
   private Long totalContentSize;
   private boolean last;
}
