package com.thiha.roomrent;

import java.util.Date;

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

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AgentRepositoryTest {
    
    @Autowired
    AgentRepository agentRepository;

    @Test
    public void testAgetRepositorySaveAgent(){
        AgentDto agentDto = AgentDto.builder().username("tester7")
                                .email("tester7@test.com")
                                .password("password")
                                .phoneNumber("091128393")
                                .profilePhoto("photolink")
                                .createdAt(new Date())
                                .role(UserRole.AGENT)
                                .build();
        Agent agent = AgentMapper.mapToAgent(agentDto);

        Agent savedAgent = agentRepository.save(agent);

        Assertions.assertThat(savedAgent).isNotNull();
        Assertions.assertThat(savedAgent.getUsername()).isEqualTo(agentDto.getUsername());
        
    }
}
