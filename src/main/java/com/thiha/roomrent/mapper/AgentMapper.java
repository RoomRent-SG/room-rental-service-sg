package com.thiha.roomrent.mapper;

import com.thiha.roomrent.dto.AgentDto;
import com.thiha.roomrent.model.Agent;

public class AgentMapper {
   public static AgentDto mapToAgentDto(Agent agent){
        return new AgentDto(
            agent.getId(),
            agent.getEmail(),
            agent.getUsername(),
            agent.getPassword(),
            agent.getPhoneNumber(),
            agent.getProfilePhoto(),
            agent.getCreatedAt(),
            agent.getRole(),
            agent.isEnabled(),
            agent.getTokens()
        );
   }

}

