package com.thiha.roomrent;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.IOException;
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
import com.thiha.roomrent.model.Agent;
import com.thiha.roomrent.model.ConfirmationToken;
import com.thiha.roomrent.repository.AgentRepository;
import com.thiha.roomrent.service.S3ImageService;
import com.thiha.roomrent.service.impl.AgentServiceImpl;
import com.thiha.roomrent.utility.DateTimeHandler;
import java.util.UUID;
import java.util.Optional;

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
    private ConfirmationToken confirmationToken;

    @BeforeEach
    public void setup() throws IOException {
        mockMultipartFile = new MockMultipartFile(
                "profileImage",
                "testimage.jpg",
                "image/jpeg",
                "test image content".getBytes());

        confirmationToken = new ConfirmationToken();
        confirmationToken.setTokenValue(UUID.randomUUID());
        confirmationToken.setCreatedAt(DateTimeHandler.getUTCNow());

        existingAgent = new Agent(
                777L,
                "tester8",
                "password",
                UserRole.AGENT,
                null,
                "tester8@test.com",
                "09222222",
                "imageUrl",
                DateTimeHandler.getUTCNow(),
                false,
                confirmationToken);
        confirmationToken.setAgent(existingAgent);

        // Mockito.when(agentRepository.save(any(Agent.class))).thenReturn(existingAgent);
        // createdAgent = agentRepository.save(existingAgent);

    }

    @Test
    public void testCreateAgent() throws IOException {
        AgentRegisterDto registerAgent = AgentRegisterDto.builder()
                .username("tester8")
                .email("tester8@test.com")
                .password("password")
                .phoneNumber("09222222")
                .profileImage(mockMultipartFile)
                .build();
        // Mock S3 image upload behavior
        doNothing().when(imageService).uploadImage(mockMultipartFile.getOriginalFilename(), mockMultipartFile);
        // mocek repo behaviour
        Mockito.when(agentRepository.save(Mockito.any(Agent.class)))
                .thenReturn(existingAgent);
        AgentDto savedAgentDto = agentService.createAgent(registerAgent);
        Assertions.assertThat(savedAgentDto).isNotNull();
        Assertions.assertThat(savedAgentDto.getUsername()).isEqualTo(registerAgent.getUsername());
        // Verify uploadImage was called
        verify(imageService).uploadImage(mockMultipartFile.getOriginalFilename(), mockMultipartFile);
    }

    @Test
    public void testUpdateExistingAgentSuccess() throws IOException {
        AgentRegisterDto newAgentRegisterDto = AgentRegisterDto.builder()
                .username("tester8")
                .email("tester8@test.com")
                .password("password")
                .phoneNumber("09440224474")
                .profileImage(mockMultipartFile)
                .build();

        // Mock S3 image upload behavior
        doNothing().when(imageService).uploadImage(mockMultipartFile.getOriginalFilename(), mockMultipartFile);

        // Mock repository
        when(agentRepository.save(any(Agent.class))).thenReturn(existingAgent);
        when(agentRepository.findById(777L)).thenReturn(Optional.of(existingAgent));
        // since there is no actual database, it will only change the passed object ^^
        AgentDto updatedAgent = agentService.updateExistingAgent(newAgentRegisterDto, 777L);

        Assertions.assertThat(updatedAgent.getPhoneNumber()).isEqualTo(newAgentRegisterDto.getPhoneNumber());
        verify(agentRepository, times(1)).save(any(Agent.class));

    }

    @Test
    public void testUpdateExistingAgentProfileImageNotFound() throws IOException {
        AgentRegisterDto newAgentRegisterDto = AgentRegisterDto.builder()
                .username("tester8")
                .email("tester8@test.com")
                .password("password")
                .phoneNumber("09440224474")
                .build();

        // since there is no actual database, it will only change the passed object ^^
        ProfileImageNotFoundException thrownException = assertThrows(ProfileImageNotFoundException.class,
                () -> {
                    agentService.updateExistingAgent(newAgentRegisterDto, 777L);
                });

        Assertions.assertThat(thrownException.getErrorMessage()).isEqualTo("Profile photo cannnot be empty");
        verify(agentRepository, never()).save(any(Agent.class));
    }

}
