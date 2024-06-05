package com.thiha.roomrent;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;
import com.thiha.roomrent.repository.AgentRepository;
import com.thiha.roomrent.service.AgentService;


@ExtendWith(MockitoExtension.class)
@TestPropertySource("classpath:application-test.properties")
public class AgentServiceTest {
   
    @InjectMocks
    private AgentService agentService;

    @Mock
    private AgentRepository agentRepository;

    // @Test
    // void testFindAgentByEmail(){
    //     AgentDto agentDto = new AgentDto();
    //     agentDto.setEmail("serviceTest@gmail.com");
    //     agentDto.setUsername("tester");
    //     agentDto.setPassword("pass");
    //     agentDto.setPhoneNumber("0923435453");
    //     agentDto.setCreatedAt(new Date());
    //     agentDto.setProfilePhoto("www.myphoto.gl");

    //     when(agentRepository.save(any(Agent.class))).thenReturn(AgentMapper.mapToAgent(agentDto));
    //     when(agentRepository.findByEmail("serviceTest@gmail.com")).thenReturn(Optional.of(AgentMapper.mapToAgent(agentDto)));

    //     AgentDto savedAgentDto = agentService.createAgent(agentDto);

    //     AgentDto retrivedAgentDto = agentService.findAgentByEmail(savedAgentDto.getEmail());
    //     assertThat(retrivedAgentDto.getEmail()).isEqualTo(savedAgentDto.getEmail());

    //     AgentDto nullAgentDto = agentService.findAgentByEmail("shouldReturnNull@gmail.com");
    //     assertThat(nullAgentDto).isEqualTo(null);

        
    // }
}
