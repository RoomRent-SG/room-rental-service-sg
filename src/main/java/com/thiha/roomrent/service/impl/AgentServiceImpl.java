package com.thiha.roomrent.service.impl;

import com.thiha.roomrent.dto.AgentDto;

public interface AgentServiceImpl{
    AgentDto createAgent(AgentDto agentDto);
    AgentDto findAgentByEmail(String email);
    AgentDto findAgentById(Long id);
    AgentDto findAgentByName(String name);
    AgentDto updateExistingAgent(AgentDto newAgentDto, AgentDto existingAgentDto );
}
