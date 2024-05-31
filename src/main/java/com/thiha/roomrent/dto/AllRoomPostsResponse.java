package com.thiha.roomrent.dto;

import java.util.List;

import lombok.Data;

@Data
public class AllRoomPostsResponse {
   private List<RoomPostDto> allRoomPosts;
   private int pageNo;
   private int pageSize;
   private Long totalContenSize;
   private boolean last;
}
