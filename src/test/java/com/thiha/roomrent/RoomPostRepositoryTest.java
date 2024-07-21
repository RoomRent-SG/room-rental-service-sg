package com.thiha.roomrent;

import com.thiha.roomrent.enums.*;
import com.thiha.roomrent.model.Agent;
import com.thiha.roomrent.model.JwtToken;
import com.thiha.roomrent.model.RoomPhoto;
import com.thiha.roomrent.model.RoomPost;
import com.thiha.roomrent.repository.AgentRepository;
import com.thiha.roomrent.repository.RoomPostRepository;
import com.thiha.roomrent.utility.DateTimeHandler;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;

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

    private RoomPost roomPost;
    private Agent agent;
    private MultipartFile mockMultipartFile;
    private List<MultipartFile> imageFiles = new ArrayList<>();

    @Before
    public void setup(){
        Agent newAgent = new Agent(1L,
                            "tester7",
                            "password",
                            UserRole.AGENT,
                            new ArrayList<JwtToken>(),
                            "tester7@tt.com",
                            "091111111",
                            "profilephoto.com",
                            DateTimeHandler.getUTCNow()
                        );
        agent = agentRepository.save(newAgent);

        mockMultipartFile = new MockMultipartFile(
                                            "profileImage",
                                            "testimage.jpg",
                                            "image/jpeg",
                                            "test image content".getBytes()
                                    );
        imageFiles.add(mockMultipartFile);


        roomPost = RoomPost.builder()
                            .id(1L)
                            .agent(agent)
                            .airConTime(AirConTime.LIMITED)
                            .allowVisitor(true)
                            .address("home address")
                            .cookingAllowance(CookingAllowance.COOKING_ALLOWED)
                            .thumbnailImage("imageUrl")
                            .description("description")
                            .location(Location.BALESTIER)
                            .postedAt(DateTimeHandler.getUTCNow())
                            .price(3000.0)
                            .propertyType(PropertyType.CONDO)
                            .roomPhotos(new ArrayList<RoomPhoto>())
                            .roomType(RoomType.MASTER_ROOM)
                            .sharePub(SharePub.INCLUSIVE)
                            .stationName(StationName.BAYFRONT)
                            .totalPax(1)
                            .build();
    }

    @Test
    public void testSaveRoomPostSucceed() {
        RoomPost roomPostToSave = roomPost;

        RoomPost savedRoomPost = roomPostRepository.save(roomPostToSave);

        Assertions.assertThat(savedRoomPost).isNotNull();

    }

    @Test
    public void testSaveRoomPostWithoutLocationAttributeThrowsInvalidConstraintException(){
        roomPost.setLocation(null);

        RoomPost newRoomPost = roomPost;
        newRoomPost.setArchived(true);
        System.out.println("isArchived"+newRoomPost.isArchived());
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class,
                                                    ()-> {
                                                        roomPostRepository.save(newRoomPost);
                                                    });
        Assertions.assertThat(exception).isExactlyInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void deleteRoomPostSucceed(){
        RoomPost roomPostToSave = roomPost;
        RoomPost savedRoomPost = roomPostRepository.save(roomPostToSave);

        Assertions.assertThat(savedRoomPost.getAgent().getId()).isEqualTo(agent.getId());

        roomPostRepository.delete(savedRoomPost);

        Optional<RoomPost> deletedRoomPost = roomPostRepository.findById(savedRoomPost.getId());
        Assertions.assertThat(deletedRoomPost.isPresent()).isFalse();
    }

    @Test
    public void getRoomPostByAgentIdSucceed(){
        RoomPost roomPostToSave = roomPost;
        RoomPost savedRoomPost = roomPostRepository.save(roomPostToSave);

        List<RoomPost> roomPostsList = roomPostRepository.findAllRoomPostsByAgentId(agent.getId());

        Assertions.assertThat(roomPostsList.size()).isEqualTo(1);
        Assertions.assertThat(roomPostsList.get(0).getLocation()).isEqualTo(savedRoomPost.getLocation());
    }

    @Test
    public void getActiveRoomPostsReturnEmptyList(){
        RoomPost roomPostToSave = roomPost;
        roomPostToSave.setArchived(true);
        roomPostRepository.save(roomPostToSave);

        List<RoomPost> roomPostsList = roomPostRepository.findActiveRoomPostsByAgentId(agent.getId());

        Assertions.assertThat(roomPostsList.size()).isEqualTo(0);
    }

    @Test
    public void getActiveRoomPostByAgentIdSucceed(){
        RoomPost roomPostToSave = roomPost;

        roomPostToSave.setArchived(false);
        RoomPost savedRoomPost = roomPostRepository.save(roomPostToSave);

        List<RoomPost> roomPostsList = roomPostRepository.findActiveRoomPostsByAgentId(agent.getId());

        Assertions.assertThat(roomPostsList.size()).isEqualTo(1);
        Assertions.assertThat(roomPostsList.get(0).getLocation()).isEqualTo(savedRoomPost.getLocation());
    }

    @Test
    public void getArchivedRoomPostByAgentIdReturnRoomPostList(){
        RoomPost roomPostToSave = roomPost;
        roomPostToSave.setArchived(true);
        RoomPost savedRoomPost = roomPostRepository.save(roomPostToSave);

        List<RoomPost> roomPostsList = roomPostRepository.findArchivedRoomPostsByAgentId(agent.getId());
        Assertions.assertThat(roomPostsList.size()).isEqualTo(1);
        Assertions.assertThat(roomPostsList.get(0).getLocation()).isEqualTo(savedRoomPost.getLocation());
    }

    @Test
    public void getArchivedRoomPostByAgentIdReturnEmptyList(){
        RoomPost roomPostToSave = roomPost;
        roomPostToSave.setArchived(false);
        roomPostRepository.save(roomPostToSave);

        List<RoomPost> roomPostsList = roomPostRepository.findArchivedRoomPostsByAgentId(agent.getId());
        roomPostsList = roomPostRepository.findArchivedRoomPostsByAgentId(agent.getId());
        Assertions.assertThat(roomPostsList.size()).isEqualTo(0);
    }
}
