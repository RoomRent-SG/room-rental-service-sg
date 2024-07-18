package com.thiha.roomrent;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.thiha.roomrent.dto.AgentDto;
import com.thiha.roomrent.enums.UserRole;
import com.thiha.roomrent.mapper.AgentMapper;
import com.thiha.roomrent.model.Agent;
import com.thiha.roomrent.repository.AgentRepository;
import com.thiha.roomrent.utility.DateTimeHandler;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AgentRepositoryTest {
    
    @Autowired
    AgentRepository agentRepository;

    @Test
    public void testAgentRepositorySaveAgent(){
        AgentDto agentDto = AgentDto.builder().username("tester7")
                                .email("tester7@test.com")
                                .password("password")
                                .phoneNumber("091128393")
                                .profilePhoto("photolink")
                                .createdAt(DateTimeHandler.getUTCNow())
                                .role(UserRole.AGENT)
                                .build();
        Agent agent = AgentMapper.mapToAgent(agentDto);

        Agent savedAgent = agentRepository.save(agent);

        Assertions.assertThat(savedAgent).isNotNull();
        Assertions.assertThat(savedAgent.getUsername()).isEqualTo(agentDto.getUsername());
        
    }

    @Test
    public void testAgentRepositoryFindAllAgents(){
        AgentDto agentDto1 = AgentDto.builder().username("tester7")
                                .email("tester7@test.com")
                                .password("password")
                                .phoneNumber("091128393")
                                .profilePhoto("photolink")
                                .createdAt(DateTimeHandler.getUTCNow())
                                .role(UserRole.AGENT)
                                .build();
        Agent agent1 = AgentMapper.mapToAgent(agentDto1);
        AgentDto agentDto = AgentDto.builder().username("tester8")
                                .email("tester8@test.com")
                                .password("password")
                                .phoneNumber("091128393")
                                .profilePhoto("photolink")
                                .createdAt(DateTimeHandler.getUTCNow())
                                .role(UserRole.AGENT)
                                .build();
        Agent agent = AgentMapper.mapToAgent(agentDto);

        agentRepository.save(agent);

        agentRepository.save(agent1);

        List<Agent> allAgents = agentRepository.findAll();

        Assertions.assertThat(allAgents.size()).isEqualTo(2);
    }

    @Test
    public void testFindAgentByEmail(){
        AgentDto agentDto = AgentDto.builder().username("tester8")
                                .email("tester8@test.com")
                                .password("password")
                                .phoneNumber("091128393")
                                .profilePhoto("photolink")
                                .createdAt(DateTimeHandler.getUTCNow())
                                .role(UserRole.AGENT)
                                .build();
        Agent agent = AgentMapper.mapToAgent(agentDto);

        agentRepository.save(agent);

        Agent returnAgent = agentRepository.findByEmail(agentDto.getEmail()).get();

        Assertions.assertThat(returnAgent).isNotNull();
        Assertions.assertThat(returnAgent.getEmail()).isEqualTo(agentDto.getEmail());

    }

    @Test
    public void testFindAgentById(){
        AgentDto agentDto = AgentDto.builder().username("tester8")
                                .email("tester8@test.com")
                                .password("password")
                                .phoneNumber("091128393")
                                .profilePhoto("photolink")
                                .createdAt(DateTimeHandler.getUTCNow())
                                .role(UserRole.AGENT)
                                .build();
        Agent agent = AgentMapper.mapToAgent(agentDto);

        Agent savedAgent = agentRepository.save(agent);

        Agent returnAgent = agentRepository.findById(savedAgent.getId()).get();

        Assertions.assertThat(returnAgent).isNotNull();
        Assertions.assertThat(returnAgent.getId()).isEqualTo(savedAgent.getId());
    }

    @Test
    public void testFindAgentByUsername(){
        AgentDto agentDto = AgentDto.builder().username("tester8")
                                .email("tester8@test.com")
                                .password("password")
                                .phoneNumber("091128393")
                                .profilePhoto("photolink")
                                .createdAt(DateTimeHandler.getUTCNow())
                                .role(UserRole.AGENT)
                                .build();
        Agent agent = AgentMapper.mapToAgent(agentDto);

        Agent savedAgent = agentRepository.save(agent);

        Agent returnAgent = agentRepository.findByUsername(savedAgent.getUsername()).get();

        Assertions.assertThat(returnAgent).isNotNull();
        Assertions.assertThat(returnAgent.getUsername()).isEqualTo(savedAgent.getUsername());
    }

}
