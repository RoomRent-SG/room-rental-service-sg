package com.thiha.roomrent.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.thiha.roomrent.dto.AgentDto;
import com.thiha.roomrent.mapper.AgentMapper;
import com.thiha.roomrent.model.Agent;
import com.thiha.roomrent.repository.AgentRepository;
import com.thiha.roomrent.service.AgentService;
import com.thiha.roomrent.service.impl.AgentServiceImpl;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AgentService implements AgentServiceImpl{
    private AgentRepository agentRepository;
    private PasswordEncoder passwordEncoder;
    @Override
    public AgentDto createAgent(AgentDto agentDto) {
        String hashedPassword = passwordEncoder.encode(agentDto.getPassword());
        agentDto.setPassword(hashedPassword);
        Agent agent = AgentMapper.mapToAgent(agentDto);
        Agent savedAgent = agentRepository.save(agent);
        return AgentMapper.mapToAgentDto(savedAgent);
    }
    @Override
    public AgentDto findAgentByEmail(String email) {
        Optional<Agent> agent = agentRepository.findByEmail(email);
        if (agent.isPresent()) {
            return AgentMapper.mapToAgentDto(agent.get());
        }else{
            return null;
        }
    }

    @Override
    public AgentDto findAgentById(Long id) {
        Optional<Agent> agentOptional = agentRepository.findById(id);
        if ((agentOptional.isPresent())) {
            return AgentMapper.mapToAgentDto(agentOptional.get());
        }
        return null;
    }

    @Override
    public AgentDto findAgentByName(String name) {
        Optional<Agent> optionalAgent = agentRepository.findByUsername(name);
        if(optionalAgent.isPresent()){
            return AgentMapper.mapToAgentDto(optionalAgent.get());
        }
        return null;
    }
    @Override
    public AgentDto updateExistingAgent(AgentDto newAgentDto, AgentDto existingAgentDto) {
        existingAgentDto.setPhoneNumber(newAgentDto.getPhoneNumber());
        existingAgentDto.setProfilePhoto(newAgentDto.getProfilePhoto());
        return AgentMapper.mapToAgentDto(agentRepository.save(AgentMapper.mapToAgent(existingAgentDto)));
    }
   
}
