package com.thiha.roomrent;

import java.util.List;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.thiha.roomrent.model.ConfirmationToken;
import com.thiha.roomrent.enums.UserRole;
import com.thiha.roomrent.model.Agent;
import com.thiha.roomrent.repository.AgentRepository;
import com.thiha.roomrent.utility.DateTimeHandler;
import org.junit.jupiter.api.BeforeEach;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AgentRepositoryTest {
    
    @Autowired
    AgentRepository agentRepository;

    private ConfirmationToken confirmationToken;

    @BeforeEach void setup(){
        confirmationToken = new ConfirmationToken();
        confirmationToken.setTokenValue(UUID.randomUUID());
        confirmationToken.setCreatedAt(DateTimeHandler.getUTCNow());
    }

    @Test
    public void testAgentRepositorySaveAgent(){
        Agent agent = new Agent(
            null,
            "tester7",
            "password",
            UserRole.AGENT,
            null,
            "tester7@test.com",
            "091128393",
            "imageUrl",
            DateTimeHandler.getUTCNow(),
            false,
            confirmationToken
        );
        confirmationToken.setAgent(agent);


        Agent savedAgent = agentRepository.save(agent);

        Assertions.assertThat(savedAgent).isNotNull();
        Assertions.assertThat(savedAgent.getUsername()).isEqualTo(agent.getUsername());
        
    }

    @Test
    public void testAgentRepositoryFindAllAgents(){

        ConfirmationToken token1 = new ConfirmationToken();
        token1.setTokenValue(UUID.randomUUID());
        token1.setCreatedAt(DateTimeHandler.getUTCNow());

        Agent agent1 = new Agent(
            null,
            "tester7",
            "password",
            UserRole.AGENT,
            null,
            "tester7@test.com",
            "091128393",
            "imageUrl",
            DateTimeHandler.getUTCNow(),
            false,
            token1
        );
        token1.setAgent(agent1);

        ConfirmationToken token2 = new ConfirmationToken();
        token2.setTokenValue(UUID.randomUUID());
        token2.setCreatedAt(DateTimeHandler.getUTCNow());
        
        Agent agent2 = new Agent(
            null,
            "tester2",
            "password",
            UserRole.AGENT,
            null,
            "tester2@test.com",
            "091128223",
            "imageUrl",
            DateTimeHandler.getUTCNow(),
            false,
            token2
        );
        token2.setAgent(agent2);

        agentRepository.save(agent1);

        agentRepository.save(agent2);

        List<Agent> allAgents = agentRepository.findAll();

        Assertions.assertThat(allAgents.size()).isEqualTo(2);
    }

    @Test
    public void testFindAgentByEmail(){
        String email = "tester7@test.com";
        Agent agent = new Agent(
            null,
            "tester7",
            "password",
            UserRole.AGENT,
            null,
            "tester7@test.com",
            "091128393",
            "imageUrl",
            DateTimeHandler.getUTCNow(),
            false,
            new ConfirmationToken()
        );
        confirmationToken.setAgent(agent);

        agentRepository.save(agent);

        Agent returnAgent = agentRepository.findByEmail(email).get();

        Assertions.assertThat(returnAgent).isNotNull();
        Assertions.assertThat(returnAgent.getEmail()).isEqualTo(email);

    }

    @Test
    public void testFindAgentById(){
        Agent agent = new Agent(
            null,
            "tester7",
            "password",
            UserRole.AGENT,
            null,
            "tester7@test.com",
            "091128393",
            "imageUrl",
            DateTimeHandler.getUTCNow(),
            false,
            new ConfirmationToken()
        );
        confirmationToken.setAgent(agent);

        Agent savedAgent = agentRepository.save(agent);

        Agent returnAgent = agentRepository.findById(savedAgent.getId()).get();

        Assertions.assertThat(returnAgent).isNotNull();
        Assertions.assertThat(returnAgent.getId()).isEqualTo(savedAgent.getId());
    }

    @Test
    public void testFindAgentByUsername(){
        Agent agent = new Agent(
            null,
            "tester7",
            "password",
            UserRole.AGENT,
            null,
            "tester7@test.com",
            "091128393",
            "imageUrl",
            DateTimeHandler.getUTCNow(),
            false,
            new ConfirmationToken()
        );
        confirmationToken.setAgent(agent);

        Agent savedAgent = agentRepository.save(agent);

        Agent returnAgent = agentRepository.findByUsername(savedAgent.getUsername()).get();

        Assertions.assertThat(returnAgent).isNotNull();
        Assertions.assertThat(returnAgent.getUsername()).isEqualTo(savedAgent.getUsername());
    }

}
