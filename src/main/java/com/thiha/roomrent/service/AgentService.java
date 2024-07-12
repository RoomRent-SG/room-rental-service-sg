package com.thiha.roomrent.service;

import java.util.List;

import com.thiha.roomrent.dto.AgentDto;
import com.thiha.roomrent.dto.AgentRegisterDto;

public interface AgentService{
    AgentDto createAgent(AgentRegisterDto registeredAgent);
    AgentDto findAgentByEmail(String email);
    AgentDto findAgentById(Long id);
    AgentDto findAgentByName(String name);
    AgentDto updateExistingAgent(AgentRegisterDto newAgentDto, AgentDto existingAgentDto );
    List<AgentDto> findAllAgents();
}
