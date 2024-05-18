package com.thiha.roomrent.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import com.thiha.roomrent.dto.RoomPostDto;
import com.thiha.roomrent.mapper.RoomPostMapper;
import com.thiha.roomrent.model.Agent;
import com.thiha.roomrent.model.RoomPost;
import com.thiha.roomrent.repository.RoomPostRepository;
import com.thiha.roomrent.service.impl.RoomPostServiceImpl;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RoomPostService implements RoomPostServiceImpl{
    private RoomPostRepository roomPostRepository;

    @Override
    public RoomPostDto createRoomPost(RoomPostDto roomPostDto, Agent agent) {
        RoomPost roomPost = new RoomPost();
        roomPost.setAirConTime(roomPostDto.getAirConTime());
        roomPost.setAllowVisitor(roomPostDto.isAllowVisitor());
        roomPost.setCookingAllowance(roomPostDto.getCookingAllowance());
        roomPost.setLocation(roomPostDto.getLocation());
        roomPost.setPassType(roomPostDto.getPassType());
        roomPost.setPropertyType(roomPostDto.getPropertyType());
        roomPost.setRoomType(roomPostDto.getRoomType());
        roomPost.setSharePub(roomPostDto.getSharePub());
        roomPost.setStationName(roomPostDto.getStationName());
        roomPost.setTotalPax(roomPostDto.getTotalPax());
        roomPost.setAgent(agent);
        RoomPost savedRoomPost = roomPostRepository.save(roomPost);

        return RoomPostMapper.mapToRoomPostDto(savedRoomPost);
    }

    @Override
    public RoomPostDto findRoomPostById(Long id) {
        Optional<RoomPost> optionalRoomPost = roomPostRepository.findById(id);
        if(optionalRoomPost.isPresent()){
            return RoomPostMapper.mapToRoomPostDto(optionalRoomPost.get());
        }
        return null;
    }

    @Override
    public RoomPostDto updateRoomPost(RoomPostDto originalRoomPost, RoomPostDto updateRoomPost) {
        //  private Long id;
        // private StationName stationName;
        // private RoomType roomType;
        // private int totalPax;
        // private CookingAllowance cookingAllowance;
        // private SharePub sharePub;
        // private AirConTime airConTime;
        // private boolean allowVisitor;
        // private PassType passType;
        // private Location location;
        // private PropertyType propertyType;
        // @JsonIgnore
        // private Agent agent;
        originalRoomPost.setStationName(updateRoomPost.getStationName());
        originalRoomPost.setRoomType(updateRoomPost.getRoomType());
        originalRoomPost.setTotalPax(updateRoomPost.getTotalPax());
        originalRoomPost.setCookingAllowance(updateRoomPost.getCookingAllowance());
        originalRoomPost.setSharePub(updateRoomPost.getSharePub());
        originalRoomPost.setAirConTime(updateRoomPost.getAirConTime());
        originalRoomPost.setAllowVisitor(updateRoomPost.isAllowVisitor());
        originalRoomPost.setPassType(updateRoomPost.getPassType());
        originalRoomPost.setLocation(updateRoomPost.getLocation());
        originalRoomPost.setPropertyType(updateRoomPost.getPropertyType());
        RoomPost savedRoomPost = roomPostRepository.save(RoomPostMapper.mapToRoomPost(originalRoomPost));
        return RoomPostMapper.mapToRoomPostDto(savedRoomPost);
    }

    
    
}
