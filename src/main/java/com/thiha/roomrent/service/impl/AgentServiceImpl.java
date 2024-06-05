package com.thiha.roomrent.service.impl;

import com.thiha.roomrent.dto.AgentDto;
import com.thiha.roomrent.dto.AgentRegisterDto;

public interface AgentServiceImpl{
    AgentDto createAgent(AgentRegisterDto registeredAgent);
    AgentDto findAgentByEmail(String email);
    AgentDto findAgentById(Long id);
    AgentDto findAgentByName(String name);
    AgentDto updateExistingAgent(AgentDto newAgentDto, AgentDto existingAgentDto );
}
