package com.thiha.roomrent.mapper;

import com.thiha.roomrent.dto.RoomPostDto;
import com.thiha.roomrent.dto.RoomPostRegisterDto;
import com.thiha.roomrent.model.RoomPost;

public class RoomPostMapper {

   public static RoomPostDto mapToRoomPostDto(RoomPost roomPost){
    return new RoomPostDto(roomPost.getId(),
                        roomPost.getStationName(),
                        roomPost.getPrice(),
                        roomPost.getRoomType(),
                        roomPost.getTotalPax(),
                        roomPost.getCookingAllowance(),
                        roomPost.getSharePub(),
                        roomPost.getAirConTime(),
                        roomPost.isAllowVisitor(),
                        roomPost.getLocation(),
                        roomPost.getPropertyType(),
                        roomPost.getDescription(),
                        roomPost.getAgent(),
                        roomPost.getPostedAt(),
                        roomPost.getRoomPhotos());
   }

   public static RoomPost mapToRoomPost(RoomPostDto roomPostDto){
    return new RoomPost(roomPostDto.getId(),
                            roomPostDto.getStationName(),
                            roomPostDto.getPrice(),
                            roomPostDto.getPostedAt(),
                            roomPostDto.getRoomType(),
                            roomPostDto.getTotalPax(),
                            roomPostDto.getCookingAllowance(),
                            roomPostDto.getSharePub(),
                            roomPostDto.getAirConTime(),
                            roomPostDto.isAllowVisitor(),
                            roomPostDto.getLocation(),
                            roomPostDto.getPropertyType(),
                            roomPostDto.getDescription(),
                            roomPostDto.getAgent(),
                            roomPostDto.getRoomPhotos());
   }

   public static RoomPostDto mapToRoomPostDtoFromRoomPostRegisterDto(RoomPostRegisterDto registerDto){
      return new RoomPostDto(registerDto.getId(),
                        registerDto.getStationName(),
                        registerDto.getPrice(),
                        registerDto.getRoomType(),
                        registerDto.getTotalPax(),
                        registerDto.getCookingAllowance(),
                        registerDto.getSharePub(),
                        registerDto.getAirConTime(),
                        registerDto.isAllowVisitor(),
                        registerDto.getLocation(),
                        registerDto.getPropertyType(),
                        registerDto.getDescription(),
                        registerDto.getAgent(),
                        registerDto.getPostedAt(),
                        registerDto.getRoomPhotos());
   }
}
