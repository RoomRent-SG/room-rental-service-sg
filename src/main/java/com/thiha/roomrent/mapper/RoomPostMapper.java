package com.thiha.roomrent.mapper;

import com.thiha.roomrent.dto.RoomPostDto;
import com.thiha.roomrent.model.RoomPost;

public class RoomPostMapper {

   public static RoomPostDto mapToRoomPostDto(RoomPost roomPost){
    return new RoomPostDto(roomPost.getId(),
                        roomPost.getStationName(),
                        roomPost.getRoomType(),
                        roomPost.getTotalPax(),
                        roomPost.getCookingAllowance(),
                        roomPost.getSharePub(),
                        roomPost.getAirConTime(),
                        roomPost.isAllowVisitor(),
                        roomPost.getPassType(),
                        roomPost.getLocation(),
                        roomPost.getPropertyType(),
                        roomPost.getAgent());
   }

   public static RoomPost mapToRoomPost(RoomPostDto roomPostDto){
    return new RoomPost(roomPostDto.getId(),
                            roomPostDto.getStationName(),
                            roomPostDto.getRoomType(),
                            roomPostDto.getTotalPax(),
                            roomPostDto.getCookingAllowance(),
                            roomPostDto.getSharePub(),
                            roomPostDto.getAirConTime(),
                            roomPostDto.isAllowVisitor(),
                            roomPostDto.getPassType(),
                            roomPostDto.getLocation(),
                            roomPostDto.getPropertyType(),
                            roomPostDto.getAgent());
   }
}
