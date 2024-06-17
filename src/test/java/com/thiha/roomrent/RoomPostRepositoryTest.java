package com.thiha.roomrent;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import com.thiha.roomrent.dto.RoomPostDto;
import com.thiha.roomrent.dto.RoomPostRegisterDto;
import com.thiha.roomrent.enums.AirConTime;
import com.thiha.roomrent.enums.CookingAllowance;
import com.thiha.roomrent.enums.Location;
import com.thiha.roomrent.enums.PropertyType;
import com.thiha.roomrent.enums.SharePub;
import com.thiha.roomrent.enums.StationName;
import com.thiha.roomrent.enums.UserRole;
import com.thiha.roomrent.mapper.RoomPostMapper;
import com.thiha.roomrent.model.Agent;
import com.thiha.roomrent.model.JwtToken;
import com.thiha.roomrent.model.RoomPhoto;
import com.thiha.roomrent.model.RoomPost;
import com.thiha.roomrent.repository.AgentRepository;
import com.thiha.roomrent.repository.RoomPostRepository;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@RunWith(SpringRunner.class)
public class RoomPostRepositoryTest {
    @Autowired
    RoomPostRepository roomPostRepository;
    @Autowired
    AgentRepository agentRepository;

    @Rule
	public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();


    private RoomPostRegisterDto registerRoomPost;
    private Agent agent;
    private MultipartFile mockMultipartFile;
    private List<MultipartFile> imageFiles = new ArrayList<>();


    @Test
    public void testSaveRoomPostSucced(){
        mockMultipartFile = new MockMultipartFile(
                                            "profileImage",
                                            "testimage.jpg",
                                            "image/jpeg",
                                            "test image content".getBytes()
                                    );
        imageFiles.add(mockMultipartFile);
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
        agentRepository.save(agent);
        
        registerRoomPost = RoomPostRegisterDto.builder()
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
                                .roomPhotoFiles(imageFiles)
                                .roomPhotos(new ArrayList<RoomPhoto>())
                                .sharePub(SharePub.INCLUSIVE)
                                .stationName(StationName.BUGIS)
                                .totalPax(2)
                                .build();
        RoomPostDto roomPostDto = RoomPostMapper.mapToRoomPostDtoFromRoomPostRegisterDto(registerRoomPost);
        RoomPost roomPostToSave = RoomPostMapper.mapToRoomPost(roomPostDto);

        RoomPost savedRoomPost = roomPostRepository.save(roomPostToSave);

        Assertions.assertThat(savedRoomPost).isNotNull();
        
    }
}
