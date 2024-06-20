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
import java.util.Date;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.thiha.roomrent.dto.AgentDto;
import com.thiha.roomrent.dto.RoomPostDto;
import com.thiha.roomrent.dto.RoomPostRegisterDto;
import com.thiha.roomrent.enums.AirConTime;
import com.thiha.roomrent.enums.CookingAllowance;
import com.thiha.roomrent.enums.Location;
import com.thiha.roomrent.enums.PropertyType;
import com.thiha.roomrent.enums.RoomType;
import com.thiha.roomrent.enums.SharePub;
import com.thiha.roomrent.enums.StationName;
import com.thiha.roomrent.enums.UserRole;
import com.thiha.roomrent.exceptions.EntityNotFoundException;
import com.thiha.roomrent.mapper.AgentMapper;
import com.thiha.roomrent.model.Agent;
import com.thiha.roomrent.model.JwtToken;
import com.thiha.roomrent.model.RoomPhoto;
import com.thiha.roomrent.model.RoomPost;
import com.thiha.roomrent.repository.AgentRepository;
import com.thiha.roomrent.repository.RoomPostRepository;
import com.thiha.roomrent.service.RoomPostService;
import com.thiha.roomrent.service.S3ImageService;

@ExtendWith(MockitoExtension.class)
public class RoomPostServiceTest {

    @InjectMocks
    private RoomPostService roomPostService;

    @Mock
    private RoomPostRepository roomPostRepository;

    @Mock
    private AgentRepository agentRepository;

    @Mock
    private S3ImageService imageService;

    @Rule
	public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    private RoomPostRegisterDto roomPostRegisterDto;
    private RoomPost roomPost;
    private Agent agent;
    private MultipartFile mockMultipartFile;
    private List<MultipartFile> imageFiles = new ArrayList<>();

    @BeforeEach
    public void setup(){
        agent = new Agent(1L,
                            "tester7",
                            "password",
                            UserRole.AGENT,
                            new ArrayList<JwtToken>(),
                            "tester7@tt.com",
                            "091111111",
                            "profilephoto.com",
                            new Date()
                        );

        mockMultipartFile = new MockMultipartFile(
                                            "profileImage",
                                            "testimage.jpg",
                                            "image/jpeg",
                                            "test image content".getBytes()
                                    );
        imageFiles.add(mockMultipartFile);

        roomPostRegisterDto = RoomPostRegisterDto.builder()
                                .agent(agent)
                                .airConTime(AirConTime.UNLIMITED)
                                .allowVisitor(true)
                                .cookingAllowance(CookingAllowance.COOKING_ALLOWED)
                                .description("MasterRoom for rent")
                                .id(11L)
                                .location(Location.BALESTIER)
                                .postedAt(new Date())
                                .price(1600.0)
                                .propertyType(PropertyType.CONDO)
                                .roomType(RoomType.MASTER_ROOM)
                                .roomPhotoFiles(imageFiles)
                                .roomPhotos(new ArrayList<RoomPhoto>())
                                .sharePub(SharePub.INCLUSIVE)
                                .stationName(StationName.BUGIS)
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
                    .location(Location.BALESTIER)
                    .postedAt(new Date())
                    .price(1600.0)
                    .propertyType(PropertyType.CONDO)
                    .roomType(RoomType.MASTER_ROOM)
                    .roomPhotos(new ArrayList<RoomPhoto>())
                    .sharePub(SharePub.INCLUSIVE)
                    .stationName(StationName.BUGIS)
                    .totalPax(2)
                    .build();
    }

    @Test
    public void createRoomPostSucceed() throws IOException{
        //mock roompostrepo
        when(roomPostRepository.save(any(RoomPost.class))).thenReturn(roomPost);

        //mock s3ImageSerive
        doNothing().when(imageService).uploadImage(anyString(), any(MultipartFile.class));
        RoomPostDto createdRoomPostDto = roomPostService.createRoomPost(roomPostRegisterDto, agent);
        
        verify(imageService, times(1)).uploadImage(anyString(), any(MultipartFile.class));
        Assertions.assertThat(createdRoomPostDto.getId()).isEqualTo(roomPost.getId());
    }

    @Test
    public void findRoomPostByIdSucced() throws IOException{
       //mock roompostrepo
       when(roomPostRepository.save(any(RoomPost.class))).thenReturn(roomPost);

       //mock s3ImageSerive
       doNothing().when(imageService).uploadImage(anyString(), any(MultipartFile.class));
       RoomPostDto createdRoomPostDto = roomPostService.createRoomPost(roomPostRegisterDto, agent);
       
       when(roomPostRepository.findById(anyLong())).thenReturn(Optional.of(roomPost));
       RoomPostDto existingRoomPost = roomPostService.findRoomPostById(createdRoomPostDto.getId());

       Assertions.assertThat(existingRoomPost.getDescription()).isEqualTo(createdRoomPostDto.getDescription());
    }

    @Test
    public void findRoomPostByAgentIdSuceed() throws IOException{
       //mock roompostrepo
       when(roomPostRepository.save(any(RoomPost.class))).thenReturn(roomPost);

       //mock s3ImageSerive
       doNothing().when(imageService).uploadImage(anyString(), any(MultipartFile.class));
       RoomPostDto createdRoomPostDto = roomPostService.createRoomPost(roomPostRegisterDto, agent);
       
       verify(imageService, times(1)).uploadImage(anyString(), any(MultipartFile.class));

       when(roomPostRepository.getRoomPostsByAgentId(agent.getId())).thenReturn(Arrays.asList(roomPost));

       List<RoomPostDto> roomPosts = roomPostService.getRoomPostsByAgentId(agent.getId());

       Assertions.assertThat(roomPosts.size()).isEqualTo(1);
       Assertions.assertThat(roomPosts.get(0).getAgent().getUsername()).isEqualTo(agent.getUsername());
       verify(roomPostRepository, times(1)).getRoomPostsByAgentId(agent.getId());
    }

    @Test
    public void updateRoomPostWithValidIdSucceed(){
        when(roomPostRepository.findById(anyLong())).thenReturn(Optional.of(roomPost));

        RoomPostRegisterDto updatedRegisterRoomPost = RoomPostRegisterDto.builder()
                                                    .agent(agent)
                                                    .airConTime(AirConTime.UNLIMITED)
                                                    .allowVisitor(true)
                                                    .cookingAllowance(CookingAllowance.COOKING_ALLOWED)
                                                    .description("MasterRoom for rent")
                                                    .id(11L)
                                                    .location(Location.BUKIT_TIMAH)
                                                    .postedAt(new Date())
                                                    .price(1600.0)
                                                    .propertyType(PropertyType.CONDO)
                                                    .roomType(RoomType.MASTER_ROOM)
                                                    .roomPhotoFiles(imageFiles)
                                                    .roomPhotos(new ArrayList<RoomPhoto>())
                                                    .sharePub(SharePub.INCLUSIVE)
                                                    .stationName(StationName.BUGIS)
                                                    .totalPax(2)
                                                    .build();
        when(roomPostRepository.save(any(RoomPost.class))).thenReturn(roomPost);

        try {
            doNothing().when(imageService).uploadImage(anyString(), any(MultipartFile.class));
        } catch (IOException e) {
            e.printStackTrace();
        }

        AgentDto agentDto = AgentMapper.mapToAgentDto(agent);

        RoomPostDto updatedRoomPostDto =  roomPostService.updateRoomPost(anyLong(), agentDto, updatedRegisterRoomPost);

        Assertions.assertThat(updatedRoomPostDto.getLocation()).isEqualTo(updatedRegisterRoomPost.getLocation());
        verify(roomPostRepository, times(1)).findById(anyLong());
        verify(roomPostRepository, times(1)).save(any(RoomPost.class));

    }

    @Test
    public void updateRoomPostWithInvalidIdThrowsEntityNotFoundException(){
        when(roomPostRepository.findById(anyLong())).thenReturn(Optional.empty());

        RoomPostRegisterDto updatedRegisterRoomPost = RoomPostRegisterDto.builder()
                                                    .agent(agent)
                                                    .airConTime(AirConTime.UNLIMITED)
                                                    .allowVisitor(true)
                                                    .cookingAllowance(CookingAllowance.COOKING_ALLOWED)
                                                    .description("MasterRoom for rent")
                                                    .id(11L)
                                                    .location(Location.BUKIT_TIMAH)
                                                    .postedAt(new Date())
                                                    .price(1600.0)
                                                    .propertyType(PropertyType.CONDO)
                                                    .roomType(RoomType.MASTER_ROOM)
                                                    .roomPhotoFiles(imageFiles)
                                                    .roomPhotos(new ArrayList<RoomPhoto>())
                                                    .sharePub(SharePub.INCLUSIVE)
                                                    .stationName(StationName.BUGIS)
                                                    .totalPax(2)
                                                    .build();
        AgentDto agentDto = AgentMapper.mapToAgentDto(agent);
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                                                 ()->{
                                                    roomPostService.updateRoomPost(anyLong(), agentDto, updatedRegisterRoomPost);
                                                 });
        Assertions.assertThat(exception.getErrorMassage()).isEqualTo("RoomPost cannot be found");
        verify(roomPostRepository, times(1)).findById(anyLong());
        verify(roomPostRepository, never()).save(any(RoomPost.class));
    }

    @Test
    public void updateRoomPostWithInvalidAgentThrowsEntityNotFoundException(){
        when(roomPostRepository.findById(anyLong())).thenReturn(Optional.of(roomPost));

        RoomPostRegisterDto updatedRegisterRoomPost = RoomPostRegisterDto.builder()
                                                    .agent(agent)
                                                    .airConTime(AirConTime.UNLIMITED)
                                                    .allowVisitor(true)
                                                    .cookingAllowance(CookingAllowance.COOKING_ALLOWED)
                                                    .description("MasterRoom for rent")
                                                    .id(11L)
                                                    .location(Location.BUKIT_TIMAH)
                                                    .postedAt(new Date())
                                                    .price(1600.0)
                                                    .propertyType(PropertyType.CONDO)
                                                    .roomType(RoomType.MASTER_ROOM)
                                                    .roomPhotoFiles(imageFiles)
                                                    .roomPhotos(new ArrayList<RoomPhoto>())
                                                    .sharePub(SharePub.INCLUSIVE)
                                                    .stationName(StationName.BUGIS)
                                                    .totalPax(2)
                                                    .build();
        AgentDto invalidAgentDto = AgentDto.builder()
                                            .id(22L)
                                            .username("Invalid Agent")
                                            .email("invalidagent.tt.com")
                                            .password("password")
                                            .createdAt(new Date())
                                            .phoneNumber("134324343")
                                            .profilePhoto("profile")
                                            .role(UserRole.AGENT)
                                            .build();
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                                                 ()->{
                                                    roomPostService.updateRoomPost(anyLong(), invalidAgentDto, updatedRegisterRoomPost);
                                                 });
        Assertions.assertThat(exception.getErrorMassage()).isEqualTo("RoomPost cannot be found");
        verify(roomPostRepository, times(1)).findById(anyLong());
        verify(roomPostRepository, never()).save(any(RoomPost.class));
    }

}


