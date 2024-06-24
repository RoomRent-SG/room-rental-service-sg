package com.thiha.roomrent.service.impl;

import java.util.List;

import com.thiha.roomrent.dto.AgentDto;
import com.thiha.roomrent.dto.AgentRegisterDto;

public interface AgentServiceImpl{
    AgentDto createAgent(AgentRegisterDto registeredAgent);
    AgentDto findAgentByEmail(String email);
    AgentDto findAgentById(Long id);
    AgentDto findAgentByName(String name);
    AgentDto updateExistingAgent(AgentRegisterDto newAgentDto, AgentDto existingAgentDto );
    List<AgentDto> findAllAgents();
}
