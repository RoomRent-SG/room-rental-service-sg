package com.thiha.roomrent.service.impl;
import java.util.List;

import com.thiha.roomrent.dto.AgentDto;
import com.thiha.roomrent.dto.AllRoomPostsResponse;
import com.thiha.roomrent.dto.RoomPostDto;
import com.thiha.roomrent.dto.RoomPostRegisterDto;
import com.thiha.roomrent.dto.RoomPostSearchFilter;
import com.thiha.roomrent.model.Agent;

public interface RoomPostServiceImpl {
   RoomPostDto createRoomPost(RoomPostRegisterDto roomPostRegisterDto, Agent agent);
   RoomPostDto findRoomPostById(Long id);
   RoomPostDto updateRoomPost(Long postId,AgentDto agent, RoomPostRegisterDto updateRoomPost);
   List<RoomPostDto> getRoomPostsByAgentId(Long agentId);
   AllRoomPostsResponse getAllRoomPosts(int pageNo, int PageSize, RoomPostSearchFilter searchFilter);
   void deleteRoomPostById(Long id, AgentDto currentAgent);
}
