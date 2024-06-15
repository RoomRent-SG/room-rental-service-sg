package com.thiha.roomrent;

import static org.mockito.Mockito.when;

import java.util.Date;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.multipart.MultipartFile;

import com.thiha.roomrent.dto.AgentDto;
import com.thiha.roomrent.dto.AgentRegisterDto;
import com.thiha.roomrent.enums.UserRole;
import com.thiha.roomrent.mapper.AgentMapper;
import com.thiha.roomrent.model.Agent;
import com.thiha.roomrent.repository.AgentRepository;
import com.thiha.roomrent.service.AgentService;
import com.thiha.roomrent.service.S3ImageService;


@ExtendWith(MockitoExtension.class)
@TestPropertySource("classpath:application-test.properties")
public class AgentServiceTest {
   
    @InjectMocks
    private AgentService agentService;

    @Mock
    private AgentRepository agentRepository;

    @Mock
    private S3ImageService imageService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void testCreteAgent(){

        // Create a mock MultipartFile
        MultipartFile mockMultipartFile = new MockMultipartFile(
                "profileImage",
                "testimage.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );

        AgentRegisterDto registerAgent = AgentRegisterDto.builder()
                                                        .username("tester7")
                                                        .email("tester7@test.com")
                                                        .password("password")
                                                        .phoneNumber("091128393")
                                                        .profilePhoto("photolink")
                                                        .profileImage(mockMultipartFile)
                                                        .createdAt(new Date())
                                                        .role(UserRole.AGENT)
                                                        .build();
                                            

        AgentDto agentDto = AgentDto.builder().username("tester7")
                                            .email("tester7@test.com")
                                            .password("password")
                                            .phoneNumber("091128393")
                                            .profilePhoto("photolink")
                                            .createdAt(new Date())
                                            .role(UserRole.AGENT)
                                            .build();
        Agent agent = AgentMapper.mapToAgent(agentDto);
        when(agentRepository.save(Mockito.any(Agent.class))).thenReturn(agent);

        AgentDto savedAgentDto = agentService.createAgent(registerAgent);

        Assertions.assertThat(savedAgentDto).isNotNull();
        
    }

  
}
