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
                        roomPost.getLocation(),
                        roomPost.getPropertyType(),
                        roomPost.getDescription(),
                        roomPost.getThumbnailImage(),
                        roomPost.getAgent(),
                        roomPost.getPostedAt(),
                        roomPost.getRoomPhotos());
   }

   public static RoomPost mapToRoomPost(RoomPostDto roomPostDto){
      RoomPost roomPost = RoomPost.builder()
                              .id(roomPostDto.getId())
                              .agent(roomPostDto.getAgent())
                              .airConTime(roomPostDto.getAirConTime())
                              .allowVisitor(roomPostDto.isAllowVisitor())
                              .address(roomPostDto.getAddress())
                              .cookingAllowance(roomPostDto.getCookingAllowance())
                              .thumbnailImage(roomPostDto.getThumbnailImage())
                              .description(roomPostDto.getDescription())
                              .location(roomPostDto.getLocation())
                              .postedAt(roomPostDto.getPostedAt())
                              .price(roomPostDto.getPrice())
                              .propertyType(roomPostDto.getPropertyType())
                              .roomPhotos(roomPostDto.getRoomPhotos())
                              .roomType(roomPostDto.getRoomType())
                              .sharePub(roomPostDto.getSharePub())
                              .stationName(roomPostDto.getStationName())
                              .totalPax(roomPostDto.getTotalPax())
                              .build();
    return roomPost;
   }



   public static RoomPostListDto mapToRoomPostListDto(RoomPost roomPost){
      return new RoomPostListDto(
         roomPost.getId(),
         roomPost.getPrice(),
         roomPost.getThumbnailImage(),
         roomPost.getLocation(),
         roomPost.getRoomType(),
         roomPost.getPropertyType(),
         roomPost.getStationName(),
         roomPost.getPostedAt()
      );
   }

}