package com.thiha.roomrent.service.impl;
import java.util.List;

import com.thiha.roomrent.dto.RoomPostDto;
import com.thiha.roomrent.model.Agent;

public interface RoomPostServiceImpl {
   RoomPostDto createRoomPost(RoomPostDto roomPostDto, Agent agent);
   RoomPostDto findRoomPostById(Long id);
   RoomPostDto updateRoomPost(RoomPostDto originalRoomPost, RoomPostDto updateRoomPost);
   List<RoomPostDto> getRoomPostsByAgentId(Long agentId);
   void deleteRoomPostById(Long id);
}
