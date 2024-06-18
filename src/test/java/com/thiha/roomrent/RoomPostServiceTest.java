package com.thiha.roomrent;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        Assertions.assertThat(createdRoomPostDto.getId()).isEqualTo(roomPost.getId());
    }

    
}
