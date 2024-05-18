package com.thiha.roomrent.service.impl;
import com.thiha.roomrent.dto.RoomPostDto;
import com.thiha.roomrent.model.Agent;

public interface RoomPostServiceImpl {
   RoomPostDto createRoomPost(RoomPostDto roomPostDto, Agent agent);
   RoomPostDto findRoomPostById(Long id);
   RoomPostDto updateRoomPost(RoomPostDto originalRoomPost, RoomPostDto updateRoomPost);
}
