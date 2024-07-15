package com.thiha.roomrent;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.jupiter.api.BeforeEach;
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
import com.thiha.roomrent.exceptions.ProfileImageNotFoundException;
import com.thiha.roomrent.mapper.AgentMapper;
import com.thiha.roomrent.model.Agent;
import com.thiha.roomrent.model.JwtToken;
import com.thiha.roomrent.repository.AgentRepository;
import com.thiha.roomrent.service.S3ImageService;
import com.thiha.roomrent.service.impl.AgentServiceImpl;
import com.thiha.roomrent.utility.DateTimeHandler;


@ExtendWith(MockitoExtension.class)
@TestPropertySource("classpath:application-test.properties")
public class AgentServiceTest {
   
    @InjectMocks
    private AgentServiceImpl agentService;

    @Mock
    private AgentRepository agentRepository;

    @Mock
    private S3ImageService imageService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Rule
	public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    private MultipartFile mockMultipartFile;
    private Agent existingAgent;

    @BeforeEach
    public void setup() throws IOException{
        mockMultipartFile = new MockMultipartFile(
                                            "profileImage",
                                            "testimage.jpg",
                                            "image/jpeg",
                                            "test image content".getBytes()
                                    );
        existingAgent = new Agent(
                            1L,
                            "tester7",
                            "password",
                            UserRole.AGENT,
                            new ArrayList<JwtToken>(),
                            "tester7@tt.com",
                            "091111111",
                            "profilephoto.com",
                            DateTimeHandler.getUTCNow()
                        );

        // Mockito.when(agentRepository.save(any(Agent.class))).thenReturn(existingAgent);
        // createdAgent =  agentRepository.save(existingAgent);
        System.out.println(existingAgent);

    }

    @Test
    public void testCreateAgent() throws IOException{
        AgentRegisterDto registerAgent = AgentRegisterDto.builder()
                                            .username("tester8")
                                            .email("tester8@test.com")
                                            .password("password")
                                            .phoneNumber("09222222")
                                            .profilePhoto("photolink")
                                            .profileImage(mockMultipartFile)
                                            .createdAt(DateTimeHandler.getUTCNow())
                                            .role(UserRole.AGENT)
                                            .build();
        // Mock S3 image upload behavior
        doNothing().when(imageService).uploadImage(mockMultipartFile.getOriginalFilename(), mockMultipartFile);
        //mocek repo behaviour
        Mockito.when(agentRepository.save(Mockito.any(Agent.class)))
                    .thenReturn(AgentMapper.mapToAgent(AgentMapper.mapToAgentDtoFromAgentRegisterDto(registerAgent)));
        AgentDto savedAgentDto = agentService.createAgent(registerAgent);
        Assertions.assertThat(savedAgentDto).isNotNull();
        Assertions.assertThat(savedAgentDto.getUsername()).isEqualTo(registerAgent.getUsername());
       // Verify uploadImage was called
        verify(imageService).uploadImage(mockMultipartFile.getOriginalFilename(), mockMultipartFile);
    }

   @Test
    public void testUpdateExistingAgentSuccess() throws IOException {
        AgentRegisterDto newAgentRegisterDto = AgentRegisterDto.builder()
                                        .username("tester7")
                                        .email("tester7@test.com")
                                        .password("password")
                                        .phoneNumber("09440224474")
                                        .profilePhoto("photolink")
                                        .profileImage(mockMultipartFile)
                                        .createdAt(DateTimeHandler.getUTCNow())
                                        .role(UserRole.AGENT)
                                        .build();
        AgentDto existigAgentDto = AgentMapper.mapToAgentDto(existingAgent);

        // Mock S3 image upload behavior
        doNothing().when(imageService).uploadImage(mockMultipartFile.getOriginalFilename(), mockMultipartFile);

        //Mock repository
        when(agentRepository.save(any(Agent.class))).thenReturn(existingAgent);
        //since there is no actual database, it will only change the passed object ^^
        agentService.updateExistingAgent(newAgentRegisterDto, existigAgentDto);

        Assertions.assertThat(existigAgentDto.getPhoneNumber()).isEqualTo(newAgentRegisterDto.getPhoneNumber());
        verify(agentRepository, times(1)).save(any(Agent.class));

    }

    @Test
    public void testUpdateExistingAgentProfileImageNotFound() throws IOException{
        AgentRegisterDto newAgentRegisterDto = AgentRegisterDto.builder()
                                        .username("tester7")
                                        .email("tester7@test.com")
                                        .password("password")
                                        .phoneNumber("09440224474")
                                        .profilePhoto("photolink")
                                        .createdAt(DateTimeHandler.getUTCNow())
                                        .role(UserRole.AGENT)
                                        .build();
        AgentDto existigAgentDto = AgentMapper.mapToAgentDto(existingAgent);
        
        //since there is no actual database, it will only change the passed object ^^
        ProfileImageNotFoundException thrownException = assertThrows(ProfileImageNotFoundException.class, 
                                    ()->{
                                        agentService.updateExistingAgent(newAgentRegisterDto, existigAgentDto);
                                    });   
        

        Assertions.assertThat(thrownException.getErrorMessage()).isEqualTo("Profile photo cannnot be empty");
        verify(agentRepository, never()).save(any(Agent.class));
    }


}

