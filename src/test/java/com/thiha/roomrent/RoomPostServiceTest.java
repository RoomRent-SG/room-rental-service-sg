package com.thiha.roomrent;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import com.thiha.roomrent.dto.AgentDto;
import com.thiha.roomrent.dto.AllRoomPostsResponse;
import com.thiha.roomrent.dto.RoomPostDto;
import com.thiha.roomrent.dto.RoomPostListDto;
import com.thiha.roomrent.dto.RoomPostRegisterDto;
import com.thiha.roomrent.enums.AirConTime;
import com.thiha.roomrent.enums.CookingAllowance;
import com.thiha.roomrent.enums.PropertyType;
import com.thiha.roomrent.enums.RoomType;
import com.thiha.roomrent.enums.SharePub;
import com.thiha.roomrent.enums.UserRole;
import com.thiha.roomrent.exceptions.EntityNotFoundException;
import com.thiha.roomrent.mapper.AgentMapper;
import com.thiha.roomrent.model.Agent;
import com.thiha.roomrent.model.ConfirmationToken;
import com.thiha.roomrent.model.Location;
import com.thiha.roomrent.model.RoomPhoto;
import com.thiha.roomrent.model.RoomPost;
import com.thiha.roomrent.repository.AgentRepository;
import com.thiha.roomrent.repository.LocationRepository;
import com.thiha.roomrent.repository.RoomPostRepository;
import com.thiha.roomrent.service.S3ImageService;
import com.thiha.roomrent.service.StationService;
import com.thiha.roomrent.service.impl.RoomPostServiceImpl;
import com.thiha.roomrent.utility.DateTimeHandler;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class RoomPostServiceTest {

    @InjectMocks
    private RoomPostServiceImpl roomPostService;

    @Mock
    private RoomPostRepository roomPostRepository;

    @Mock
    private AgentRepository agentRepository;

    @Mock
    private S3ImageService imageService;

    @Mock
    private StationService stationService;

    @Mock
    private LocationRepository locationRepository;

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    private RoomPostRegisterDto roomPostRegisterDto;
    private RoomPost roomPost;
    private Agent agent;
    private Location location;
    private AgentDto agentDto;
    private MultipartFile mockMultipartFile;
    private List<MultipartFile> imageFiles = new ArrayList<>();
    private ConfirmationToken confirmationToken;

    @BeforeEach
    public void setup() {
        confirmationToken = new ConfirmationToken();
        confirmationToken.setTokenValue(UUID.randomUUID());
        confirmationToken.setCreatedAt(DateTimeHandler.getUTCNow());
        agent = new Agent(
                1L,
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
        confirmationToken.setAgent(agent);
        agentDto = AgentMapper.mapToAgentDto(agent);
        location = new Location(1L, "Baliester", null);

        mockMultipartFile = new MockMultipartFile(
                "profileImage",
                "testimage.jpg",
                "image/jpeg",
                "test image content".getBytes());
        imageFiles.add(mockMultipartFile);

        roomPostRegisterDto = RoomPostRegisterDto.builder()
                .airConTime("Unlimited")
                .allowVisitor(true)
                .cookingAllowance("Cooking Allowed")
                .description("MasterRoom for rent")
                .location("Jurong East")
                .price(1600.0)
                .propertyType("Condominium")
                .roomType("Master Room")
                .roomPhotoFiles(imageFiles)
                .sharePub("Inclusive")
                .stationName("Bugis")
                .totalPax(2)
                .build();
        roomPost = RoomPost.builder()
                .id(1L)
                .agent(agent)
                .airConTime(AirConTime.UNLIMITED)
                .allowVisitor(true)
                .cookingAllowance(CookingAllowance.COOKING_ALLOWED)
                .description("MasterRoom for rent")
                .id(11L)
                .location(location)
                .postedAt(DateTimeHandler.getUTCNow())
                .isArchived(false)
                .price(1600.0)
                .propertyType(PropertyType.CONDO)
                .roomType(RoomType.MASTER_ROOM)
                .roomPhotos(new ArrayList<RoomPhoto>())
                .sharePub(SharePub.INCLUSIVE)
                .stationName("Bouna Vista")
                .totalPax(2)
                .build();
    }

    @Test
    public void createRoomPostSucceed() throws IOException {
        // mock roompostrepo
        when(roomPostRepository.save(any(RoomPost.class))).thenReturn(roomPost);
        when(agentRepository.findById(1L)).thenReturn(Optional.of(agent));

        // mock s3ImageSerive
        doNothing().when(imageService).uploadImage(anyString(), any(MultipartFile.class));

        // mock locationService
        when(locationRepository.findLocationByName(anyString())).thenReturn(Optional.of(location));

        RoomPostDto createdRoomPostDto = roomPostService.createRoomPost(roomPostRegisterDto, agentDto);

        verify(imageService, times(1)).uploadImage(anyString(), any(MultipartFile.class));
        Assertions.assertThat(createdRoomPostDto.getId()).isEqualTo(roomPost.getId());
    }

    @Test
    public void findRoomPostByIdSucced() throws IOException {
        when(locationRepository.findLocationByName(anyString())).thenReturn(Optional.of(location));
        when(roomPostRepository.save(any(RoomPost.class))).thenReturn(roomPost);
        when(agentRepository.findById(1L)).thenReturn(Optional.of(agent));
        // mock s3ImageSerive
        doNothing().when(imageService).uploadImage(anyString(), any(MultipartFile.class));
        RoomPostDto createdRoomPostDto = roomPostService.createRoomPost(roomPostRegisterDto, agentDto);

        when(roomPostRepository.findById(anyLong())).thenReturn(Optional.of(roomPost));
        RoomPostDto existingRoomPost = roomPostService.findRoomPostById(createdRoomPostDto.getId());

        Assertions.assertThat(existingRoomPost.getDescription()).isEqualTo(createdRoomPostDto.getDescription());
    }

    @Test
    public void findActiveRoomPostByAgentIdSuceed() throws IOException {
        when(locationRepository.findLocationByName(anyString())).thenReturn(Optional.of(location));
        when(roomPostRepository.save(any(RoomPost.class))).thenReturn(roomPost);
        when(agentRepository.findById(1L)).thenReturn(Optional.of(agent));
        // mock s3ImageSerive
        doNothing().when(imageService).uploadImage(anyString(), any(MultipartFile.class));
        roomPostService.createRoomPost(roomPostRegisterDto, agentDto);

        verify(imageService, times(1)).uploadImage(anyString(), any(MultipartFile.class));

        Pageable pageable = PageRequest.of(0, 2, Sort.by("postedAt").descending());
        Page<RoomPost> roomPostsResponse = new PageImpl<>(Arrays.asList(roomPost));
        when(roomPostRepository.findActiveRoomPostsByAgentId(agent.getId(), pageable)).thenReturn(roomPostsResponse);

        AllRoomPostsResponse roomPostResponse = roomPostService.getActiveRoomPostsByAgentId(agent.getId(), 0, 2);
        List<RoomPostListDto> roomPosts = roomPostResponse.getAllRoomPosts();
        Assertions.assertThat(roomPosts.size()).isEqualTo(1);
        verify(roomPostRepository, times(1)).findActiveRoomPostsByAgentId(agent.getId(), pageable);
    }

    @Test
    public void updateRoomPostWithValidIdSucceed() {
        when(roomPostRepository.findById(anyLong())).thenReturn(Optional.of(roomPost));

        RoomPostRegisterDto updatedRegisterRoomPost = RoomPostRegisterDto.builder()
                .airConTime("Unlimited")
                .allowVisitor(true)
                .cookingAllowance("Allowed")
                .description("MasterRoom for rent")
                .location("Bukit")
                .price(1600.0)
                .propertyType("Condominium")
                .roomType("Master Room")
                .roomPhotoFiles(imageFiles)
                .sharePub("Inclusive")
                .stationName("Bugis")
                .totalPax(2)
                .build();
        when(roomPostRepository.save(any(RoomPost.class))).thenReturn(roomPost);
        when(stationService.getStationByName("Bugis")).thenReturn("Bugis");
        when(locationRepository.findLocationByName(anyString())).thenReturn(Optional.of(location));
        try {
            doNothing().when(imageService).uploadImage(anyString(), any(MultipartFile.class));
        } catch (IOException e) {
            e.printStackTrace();
        }

        AgentDto agentDto = AgentMapper.mapToAgentDto(agent);

        RoomPostDto updatedRoomPostDto = roomPostService.updateRoomPost(anyLong(), agentDto, updatedRegisterRoomPost);

        Assertions.assertThat(updatedRoomPostDto.getAddress()).isEqualTo(updatedRegisterRoomPost.getAddress());
        verify(roomPostRepository, times(1)).findById(anyLong());
        verify(roomPostRepository, times(1)).save(any(RoomPost.class));

    }

    @Test
    public void updateRoomPostWithInvalidIdThrowsEntityNotFoundException() {
        when(roomPostRepository.findById(anyLong())).thenReturn(Optional.empty());

        RoomPostRegisterDto updatedRegisterRoomPost = RoomPostRegisterDto.builder()
                .airConTime("Unlimited")
                .allowVisitor(true)
                .cookingAllowance("Allowed")
                .description("MasterRoom for rent")
                .location("Jurong East")
                .price(1600.0)
                .propertyType("Condominium")
                .roomType("Master Room")
                .roomPhotoFiles(imageFiles)
                .sharePub("Inclusive")
                .stationName("Bugis")
                .totalPax(2)
                .build();
        AgentDto agentDto = AgentMapper.mapToAgentDto(agent);
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> {
                    roomPostService.updateRoomPost(anyLong(), agentDto, updatedRegisterRoomPost);
                });
        Assertions.assertThat(exception.getErrorMassage()).isEqualTo("RoomPost cannot be found");
        verify(roomPostRepository, times(1)).findById(anyLong());
        verify(roomPostRepository, never()).save(any(RoomPost.class));
    }

    @Test
    public void updateRoomPostWithInvalidAgentThrowsEntityNotFoundException() {
        when(roomPostRepository.findById(anyLong())).thenReturn(Optional.of(roomPost));

        RoomPostRegisterDto updatedRegisterRoomPost = RoomPostRegisterDto.builder()
                .airConTime("Unlimited")
                .allowVisitor(true)
                .cookingAllowance("Allowed")
                .description("MasterRoom for rent")
                .location("Jurong East").price(1600.0)
                .propertyType("Condominium")
                .roomType("Master Room")
                .roomPhotoFiles(imageFiles)
                .sharePub("Inclusive")
                .stationName("Bugis")
                .totalPax(2)
                .build();
        AgentDto invalidAgentDto = AgentDto.builder()
                .id(22L)
                .username("Invalid Agent")
                .email("invalidagent.tt.com")
                .password("password")
                .createdAt(DateTimeHandler.getUTCNow())
                .phoneNumber("134324343")
                .profilePhoto("profile")
                .role(UserRole.AGENT)
                .build();
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> {
                    roomPostService.updateRoomPost(anyLong(), invalidAgentDto, updatedRegisterRoomPost);
                });
        Assertions.assertThat(exception.getErrorMassage()).isEqualTo("RoomPost cannot be found");
        verify(roomPostRepository, times(1)).findById(anyLong());
        verify(roomPostRepository, never()).save(any(RoomPost.class));
    }

    @Test
    public void deleteRoomPostByIdSucceed() {
        when(roomPostRepository.save(any(RoomPost.class))).thenReturn(roomPost);

        RoomPost savedRoomPost = roomPostRepository.save(roomPost);

        Assertions.assertThat(savedRoomPost.getDescription()).isEqualTo(roomPost.getDescription());

        when(roomPostRepository.findById(1L)).thenReturn(Optional.of(roomPost));

        AgentDto agentDto = AgentMapper.mapToAgentDto(agent);
        roomPostService.deleteRoomPostById(1L, agentDto);

        verify(roomPostRepository, times(1)).findById(1L);
        verify(roomPostRepository, times(1)).deleteById(1L);

        when(roomPostRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<RoomPost> deletedRoomPost = roomPostRepository.findById(1L);
        Assertions.assertThat(deletedRoomPost).isEmpty();

    }

}
