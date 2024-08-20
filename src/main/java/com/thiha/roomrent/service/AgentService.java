package com.thiha.roomrent.service;

import java.util.List;
import java.util.UUID;
import com.thiha.roomrent.dto.AgentDto;
import com.thiha.roomrent.dto.AgentRegisterDto;

public interface AgentService{
    AgentDto createAgent(AgentRegisterDto registeredAgent);
    AgentDto enableAgent(UUID token);
    AgentDto findAgentByEmail(String email);
    AgentDto findAgentById(Long id);
    AgentDto findAgentByName(String name);
    AgentDto updateExistingAgent(AgentRegisterDto newAgentDto, Long agentId);
    List<AgentDto> findAllAgents();
}
