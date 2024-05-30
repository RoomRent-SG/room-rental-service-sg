package com.thiha.roomrent.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.thiha.roomrent.dto.RoomPostDto;
import com.thiha.roomrent.dto.RoomPostRegisterDto;
import com.thiha.roomrent.mapper.RoomPostMapper;
import com.thiha.roomrent.model.Agent;
import com.thiha.roomrent.model.RoomPhoto;
import com.thiha.roomrent.model.RoomPost;
import com.thiha.roomrent.repository.RoomPostRepository;
import com.thiha.roomrent.service.impl.RoomPostServiceImpl;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RoomPostService implements RoomPostServiceImpl{
    private RoomPostRepository roomPostRepository;
    private S3ImageService s3ImageService;

    @Override
    public RoomPostDto createRoomPost(RoomPostRegisterDto roomPostRegisterDto, Agent agent) {
        RoomPost roomPost = new RoomPost();
        roomPost.setAirConTime(roomPostRegisterDto.getAirConTime());
        roomPost.setAllowVisitor(roomPostRegisterDto.isAllowVisitor());
        roomPost.setCookingAllowance(roomPostRegisterDto.getCookingAllowance());
        roomPost.setLocation(roomPostRegisterDto.getLocation());
        roomPost.setPropertyType(roomPostRegisterDto.getPropertyType());
        roomPost.setRoomType(roomPostRegisterDto.getRoomType());
        roomPost.setSharePub(roomPostRegisterDto.getSharePub());
        roomPost.setStationName(roomPostRegisterDto.getStationName());
        roomPost.setTotalPax(roomPostRegisterDto.getTotalPax());
        roomPost.setAgent(agent);
        roomPost.setPrice(roomPostRegisterDto.getPrice()); 
        roomPost.setPostedAt(new Date());
        
        // upload images to s3
        List<MultipartFile> roomPhotoFiles = roomPostRegisterDto.getRoomPhotoFiles();
        List<RoomPhoto> roomPhotos = new ArrayList<>();
        roomPhotoFiles.forEach(image -> {
                    try{
                        s3ImageService.uploadImage(image.getOriginalFilename(), image);
                        RoomPhoto roomPhoto = new RoomPhoto();
                        roomPhoto.setFileName(image.getOriginalFilename());
                        roomPhoto.setRoomPost(roomPost);
                        roomPhotos.add(roomPhoto);
                    }catch(IOException e){
                        throw new RuntimeException();
                    }
                });
        roomPost.setRoomPhotos(roomPhotos);
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
        originalRoomPost.setStationName(updateRoomPost.getStationName());
        originalRoomPost.setRoomType(updateRoomPost.getRoomType());
        originalRoomPost.setTotalPax(updateRoomPost.getTotalPax());
        originalRoomPost.setCookingAllowance(updateRoomPost.getCookingAllowance());
        originalRoomPost.setSharePub(updateRoomPost.getSharePub());
        originalRoomPost.setAirConTime(updateRoomPost.getAirConTime());
        originalRoomPost.setAllowVisitor(updateRoomPost.isAllowVisitor());
        originalRoomPost.setLocation(updateRoomPost.getLocation());
        originalRoomPost.setPropertyType(updateRoomPost.getPropertyType());
        RoomPost savedRoomPost = roomPostRepository.save(RoomPostMapper.mapToRoomPost(originalRoomPost));
        return RoomPostMapper.mapToRoomPostDto(savedRoomPost);
    }

    @Override
    public List<RoomPostDto> getRoomPostsByAgentId(Long agentId) {
        List<RoomPost> roomPosts = roomPostRepository.getRoomPostsByAgentId(agentId);
        List<RoomPostDto> roomPostDtos = new ArrayList<>();
        for(RoomPost roomPost: roomPosts){
            roomPostDtos.add(RoomPostMapper.mapToRoomPostDto(roomPost));
        }
        return roomPostDtos;
    }

    @Override
    public void deleteRoomPostById(Long id) {
        roomPostRepository.deleteById(id);
    }

    
    
}
