package com.thiha.roomrent.mapper;

import com.thiha.roomrent.dto.RoomPostDto;
import com.thiha.roomrent.dto.RoomPostListDto;
import com.thiha.roomrent.model.RoomPost;

public class RoomPostMapper {

   public static RoomPostDto mapToRoomPostDto(RoomPost roomPost){
    return new RoomPostDto(roomPost.getId(),
                        roomPost.getStationName(),
                        roomPost.getPrice(),
                        roomPost.getAddress(),
                        roomPost.getRoomType(),
                        roomPost.getTotalPax(),
                        roomPost.getCookingAllowance(),
                        roomPost.getSharePub(),
                        roomPost.getAirConTime(),
                        roomPost.isAllowVisitor(),
                        roomPost.getLocation().getName(),
                        roomPost.getPropertyType(),
                        roomPost.getDescription(),
                        roomPost.getThumbnailImage(),
                        roomPost.getAgent(),
                        roomPost.getPostedAt(),
                        roomPost.getRoomPhotos());
   }




   public static RoomPostListDto mapToRoomPostListDto(RoomPost roomPost){
      return new RoomPostListDto(
         roomPost.getId(),
         roomPost.getPrice(),
         roomPost.getThumbnailImage(),
         roomPost.getLocation().getName(),
         roomPost.getRoomType(),
         roomPost.getPropertyType(),
         roomPost.getStationName(),
         roomPost.getPostedAt()
      );
   }

}