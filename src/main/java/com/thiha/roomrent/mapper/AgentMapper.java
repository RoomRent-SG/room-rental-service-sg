package com.thiha.roomrent.mapper;

import com.thiha.roomrent.dto.AgentDto;
import com.thiha.roomrent.dto.AgentRegisterDto;
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
            agent.getRole()
        );
   }

   public static AgentDto mapToAgentDtoFromAgentRegisterDto(AgentRegisterDto registerAgent){
    return new AgentDto(
        registerAgent.getId(),
        registerAgent.getEmail(),
        registerAgent.getUsername(),
        registerAgent.getPassword(),
        registerAgent.getPhoneNumber(),
        registerAgent.getProfilePhoto(),
        registerAgent.getCreatedAt(),
        registerAgent.getRole()
    );
   }


   public static Agent mapToAgent(AgentDto agentDto){
    //need server side validation required
    return new Agent(
        agentDto.getId(),
        agentDto.getEmail(),
        agentDto.getUsername(),
        agentDto.getPassword(),
        agentDto.getPhoneNumber(),
        agentDto.getProfilePhoto(),
        agentDto.getCreatedAt(),
        agentDto.getRole()
    );
   }
}
