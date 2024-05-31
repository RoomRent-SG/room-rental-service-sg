package com.thiha.roomrent.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.thiha.roomrent.dto.AllRoomPostsResponse;
import com.thiha.roomrent.dto.RoomPostDto;
import com.thiha.roomrent.dto.RoomPostRegisterDto;
import com.thiha.roomrent.mapper.RoomPostMapper;
import com.thiha.roomrent.model.Agent;
import com.thiha.roomrent.model.RoomPhoto;
import com.thiha.roomrent.model.RoomPost;
import com.thiha.roomrent.repository.RoomPostRepository;
import com.thiha.roomrent.service.impl.RoomPostServiceImpl;


@Service
public class RoomPostService implements RoomPostServiceImpl{
    @Autowired
    private RoomPostRepository roomPostRepository;
    @Autowired
    private S3ImageService s3ImageService;

    @Value("${aws.cloudFront}")
    private String cloudFrontUrl;

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
                        roomPhoto.setImageUrl(cloudFrontUrl+image.getOriginalFilename());
                        roomPhoto.setRoomPost(roomPost);
                        roomPhoto.setFilename(image.getOriginalFilename());
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
    public RoomPostDto updateRoomPost(RoomPostDto originalRoomPost, RoomPostRegisterDto updateRoomPost) {
        originalRoomPost.setStationName(updateRoomPost.getStationName());
        originalRoomPost.setRoomType(updateRoomPost.getRoomType());
        originalRoomPost.setTotalPax(updateRoomPost.getTotalPax());
        originalRoomPost.setCookingAllowance(updateRoomPost.getCookingAllowance());
        originalRoomPost.setSharePub(updateRoomPost.getSharePub());
        originalRoomPost.setAirConTime(updateRoomPost.getAirConTime());
        originalRoomPost.setAllowVisitor(updateRoomPost.isAllowVisitor());
        originalRoomPost.setLocation(updateRoomPost.getLocation());
        originalRoomPost.setPropertyType(updateRoomPost.getPropertyType());
        List<MultipartFile> updatedRoomImageFiles = updateRoomPost.getRoomPhotoFiles();
        List<RoomPhoto> updatedRoomPhotos = new ArrayList<>();
        List<String> tempUrls = new ArrayList<>();
        List<RoomPhoto> existingRoomPhotos = originalRoomPost.getRoomPhotos();
        List<String> existingImages = new ArrayList<>();
        for(RoomPhoto roomPhoto: existingRoomPhotos){
            existingImages.add(roomPhoto.getImageUrl());
        }
        for(MultipartFile image: updatedRoomImageFiles){
            String tempUrl = cloudFrontUrl+image.getOriginalFilename();
            tempUrls.add(tempUrl);
            if(!existingImages.contains(tempUrl)){
                // upload new room photo
                try {
                    s3ImageService.uploadImage(image.getOriginalFilename(), image);
                    RoomPhoto roomPhoto = new RoomPhoto();
                    roomPhoto.setImageUrl(tempUrl);
                    roomPhoto.setFilename(image.getOriginalFilename());
                    roomPhoto.setRoomPost(RoomPostMapper.mapToRoomPost(originalRoomPost));
                    updatedRoomPhotos.add(roomPhoto);
                } catch (IOException e) {
                    throw new RuntimeException();
                }
            }
        }


        // clean up the old images
        for(RoomPhoto roomPhoto: existingRoomPhotos){
            String existingImageUrl = roomPhoto.getImageUrl();
            if(tempUrls.contains(existingImageUrl)){
                updatedRoomPhotos.add(roomPhoto);
            }else{
                s3ImageService.deleteImage(roomPhoto.getFilename());
            }
        }
        originalRoomPost.setRoomPhotos(updatedRoomPhotos);

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

    @Override
    public AllRoomPostsResponse getAllRoomPosts(int pageNo, int PageSize, String sortedBy) {
        Pageable pageable = PageRequest.of(pageNo, PageSize, Sort.by(sortedBy));
        Page<RoomPost> roomPosts = roomPostRepository.findAll(pageable);
        List<RoomPost> listOfRoomPosts = roomPosts.getContent();
        List<RoomPostDto> listOfRoomPostDtos = new ArrayList<>();
        for(RoomPost roomPost:listOfRoomPosts){
            listOfRoomPostDtos.add(RoomPostMapper.mapToRoomPostDto(roomPost));
        }
        AllRoomPostsResponse roomPostsResponse = new AllRoomPostsResponse();
        roomPostsResponse.setAllRoomPosts(listOfRoomPostDtos);
        roomPostsResponse.setPageNo(roomPosts.getNumber());
        roomPostsResponse.setPageSize(roomPosts.getSize());
        roomPostsResponse.setTotalContenSize(roomPosts.getTotalElements());
        roomPostsResponse.setLast(roomPosts.isLast());
        return roomPostsResponse;
    }

    
    
}
