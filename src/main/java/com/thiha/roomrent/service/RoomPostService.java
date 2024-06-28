package com.thiha.roomrent.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.thiha.roomrent.dto.AgentDto;
import com.thiha.roomrent.dto.AllRoomPostsResponse;
import com.thiha.roomrent.dto.RoomPostDto;
import com.thiha.roomrent.dto.RoomPostListDto;
import com.thiha.roomrent.dto.RoomPostRegisterDto;
import com.thiha.roomrent.dto.RoomPostSearchFilter;
import com.thiha.roomrent.exceptions.EntityNotFoundException;
import com.thiha.roomrent.exceptions.RoomPhotoNotFoundException;
import com.thiha.roomrent.exceptions.RoomPhotosExceedLimitException;
import com.thiha.roomrent.exceptions.S3ImageUploadException;
import com.thiha.roomrent.mapper.RoomPostMapper;
import com.thiha.roomrent.model.Agent;
import com.thiha.roomrent.model.RoomPhoto;
import com.thiha.roomrent.model.RoomPost;
import com.thiha.roomrent.repository.RoomPostRepository;
import com.thiha.roomrent.service.impl.RoomPostServiceImpl;
import com.thiha.roomrent.specification.RoomPostSpecification;


@Service
public class RoomPostService implements RoomPostServiceImpl{
    @Autowired
    private RoomPostRepository roomPostRepository;
    @Autowired
    private S3ImageService s3ImageService;

    @Value("${aws.cloudFront}")
    private String cloudFrontUrl;

    @Override
    @CacheEvict(value = "all_room_posts", allEntries = true)
    public RoomPostDto createRoomPost(RoomPostRegisterDto roomPostRegisterDto, Agent agent) {
        RoomPost roomPost = new RoomPost();
        // upload images to s3
        List<MultipartFile> roomPhotoFiles = roomPostRegisterDto.getRoomPhotoFiles();
        if (roomPhotoFiles.size()>10) {
            throw new RoomPhotosExceedLimitException("Too many room photos");
        }
        if(roomPhotoFiles==null || roomPhotoFiles.size()==0){
            throw new RoomPhotoNotFoundException("You need to provide room photos to create room post");
        }
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
                        throw new S3ImageUploadException("Error uploading room photos to s3");
                    }
                });
        roomPost.setAddress(roomPostRegisterDto.getAddress());
        /*
         * set the first image as the thumbnail
         */
        roomPost.setThumbnailImage(roomPhotos.get(0).getImageUrl());
        roomPost.setAirConTime(roomPostRegisterDto.getAirConTime());
        roomPost.setAllowVisitor(roomPostRegisterDto.isAllowVisitor());
        roomPost.setCookingAllowance(roomPostRegisterDto.getCookingAllowance());
        roomPost.setLocation(roomPostRegisterDto.getLocation());
        roomPost.setPropertyType(roomPostRegisterDto.getPropertyType());
        roomPost.setRoomType(roomPostRegisterDto.getRoomType());
        roomPost.setSharePub(roomPostRegisterDto.getSharePub());
        roomPost.setStationName(roomPostRegisterDto.getStationName());
        roomPost.setTotalPax(roomPostRegisterDto.getTotalPax());
        roomPost.setDescription(roomPostRegisterDto.getDescription());
        roomPost.setAgent(agent);
        roomPost.setPrice(roomPostRegisterDto.getPrice()); 
        roomPost.setPostedAt(new Date());
        roomPost.setArchived(false);
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
        throw new EntityNotFoundException("Roompost cannot be found");
    }

    @Override
    public RoomPostDto findRoomPostById(Long id, AgentDto agent){
        Optional<RoomPost> optionalRoomPost = roomPostRepository.findById(id);
        if(optionalRoomPost.isPresent()){
            RoomPostDto roomPostDto = RoomPostMapper.mapToRoomPostDto(optionalRoomPost.get());
            if (roomPostDto.getAgent().getId()==agent.getId()) {
                return roomPostDto;
            }else{
                throw new EntityNotFoundException("Roompost cannot be found");
            }
        }
        throw new EntityNotFoundException("Roompost cannot be found");
    }

    @Override
    @CacheEvict(value = "all_room_posts")
    public RoomPostDto updateRoomPost(Long postId, AgentDto agent, RoomPostRegisterDto updateRoomPost) {
        Optional<RoomPost> optionalRoomPost = roomPostRepository.findById(postId);
        if(!optionalRoomPost.isPresent()){
            throw new EntityNotFoundException("RoomPost cannot be found");
        }
        RoomPost originalRoomPost = optionalRoomPost.get();
        if(!originalRoomPost.getAgent().getUsername().equals(agent.getUsername())){
            //unauthorized entity
            throw new EntityNotFoundException("RoomPost cannot be found");
        }
        originalRoomPost.setStationName(updateRoomPost.getStationName());
        originalRoomPost.setPrice(updateRoomPost.getPrice());
        originalRoomPost.setRoomType(updateRoomPost.getRoomType());
        originalRoomPost.setTotalPax(updateRoomPost.getTotalPax());
        originalRoomPost.setCookingAllowance(updateRoomPost.getCookingAllowance());
        originalRoomPost.setSharePub(updateRoomPost.getSharePub());
        originalRoomPost.setAirConTime(updateRoomPost.getAirConTime());
        originalRoomPost.setAllowVisitor(updateRoomPost.isAllowVisitor());
        originalRoomPost.setLocation(updateRoomPost.getLocation());
        originalRoomPost.setPropertyType(updateRoomPost.getPropertyType());
        originalRoomPost.setDescription(updateRoomPost.getDescription());
        List<MultipartFile> updatedRoomImageFiles = updateRoomPost.getRoomPhotoFiles();
        List<String> updatedFilenames = new ArrayList<>();
        List<RoomPhoto> existingRoomPhotos = originalRoomPost.getRoomPhotos();
        List<String> existingFilenames = new ArrayList<>();
        
        for(MultipartFile image: updatedRoomImageFiles){
            updatedFilenames.add(image.getOriginalFilename());
        }
        /*
         * Handle the photo deletion first
         */
        for(int i=0; i<existingRoomPhotos.size(); i++){
            String filename = existingRoomPhotos.get(i).getFilename();
            if(!updatedFilenames.contains(filename)){
                existingRoomPhotos.remove(i);
            }else{
                existingFilenames.add(filename);
            }
        }

        /*
         * upload new photos
         */
        for(MultipartFile image: updatedRoomImageFiles){
            String filename = image.getOriginalFilename();
            if(!existingFilenames.contains(filename)){
                try {
                    s3ImageService.uploadImage(image.getOriginalFilename(), image);
                    RoomPhoto roomPhoto = new RoomPhoto();
                    roomPhoto.setImageUrl(cloudFrontUrl+filename);
                    roomPhoto.setFilename(image.getOriginalFilename());
                    roomPhoto.setRoomPost(originalRoomPost);
                    /*
                     * update the room photo list
                     * creating new list and setting to roomPost object will raise JPASystemException 
                     */
                    existingRoomPhotos.add(roomPhoto);
                } catch (IOException e) {
                    throw new S3ImageUploadException("Error uploading room photos to s3");
                }
            }
        }
        
        originalRoomPost.setRoomPhotos(existingRoomPhotos);
        RoomPost savedRoomPost = roomPostRepository.save(originalRoomPost);

        return RoomPostMapper.mapToRoomPostDto(savedRoomPost);
    }

    @Override
    public List<RoomPostDto> getRoomPostsByAgentId(Long agentId) {
        List<RoomPost> roomPosts = roomPostRepository.findAllRoomPostsByAgentId(agentId);
        List<RoomPostDto> roomPostDtos = new ArrayList<>();
        for(RoomPost roomPost: roomPosts){
            roomPostDtos.add(RoomPostMapper.mapToRoomPostDto(roomPost));
        }
        return roomPostDtos;
    }

    @Override
    @CacheEvict(value = "all_room_posts")
    public void deleteRoomPostById(Long id, AgentDto currentAgent) {
        RoomPostDto roomPostToDelete = this.findRoomPostById(id);
        if(roomPostToDelete == null){
            throw new EntityNotFoundException("Roompost cannot be found");
        }
        if(roomPostToDelete.getAgent().getId()!=currentAgent.getId()){
            throw new EntityNotFoundException("Roompost cannot be found");
        }
        roomPostRepository.deleteById(id);
    }


    /*
     * the room posts will be sorted by postedAt attribute by defaults
     */

    @Override
    @Cacheable(value = "all_room_posts")
    public AllRoomPostsResponse getAllActiveRoomPosts(int pageNo, int pageSize, RoomPostSearchFilter searchFilter) {
        
        /*
         * must add isArchive attribute to retrieve active room posts because specification api cannot work with custom query
         */
        if(searchFilter==null){
            searchFilter = new RoomPostSearchFilter();
        }
        searchFilter.setArchived(false);

        /*
         * make sure pageNo and pageSize are positive
         */
        pageNo = pageNo<0? 0 : pageNo ;
        pageSize = pageSize<0? 10: pageSize;
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("postedAt").descending());
        Page<RoomPost> roomPosts = new PageImpl<>(new ArrayList<RoomPost>());
        List<RoomPost> listOfRoomPosts;
        List<RoomPostDto> listOfRoomPostDtos = new ArrayList<>();
        List<RoomPostListDto> listOfRoomPostListDtos = new ArrayList<>();
        AllRoomPostsResponse roomPostsResponse = new AllRoomPostsResponse();


        RoomPostSpecification specification = new RoomPostSpecification(searchFilter);
        try{
            roomPosts = roomPostRepository.findAll(specification, pageable);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        listOfRoomPosts = roomPosts.getContent();
        
        // try{
        //     listOfRoomPostListDtos = listOfRoomPosts.stream()
        //                             .map(roomPost -> RoomPostMapper.mapToRoomPostListDto(roomPost))
        //                             .collect(Collectors.toList());
        // }
        // catch(Exception e){
        //     e.printStackTrace();
        // }

        // //map to RoomPostDTO
        // for(RoomPost roomPost:listOfRoomPosts){
        //     RoomPostDto roomPostDto = RoomPostMapper.mapToRoomPostDto(roomPost); 
        //     listOfRoomPostDtos.add(roomPostDto);
        //     /*
        //      * load the roomphots data manually because they are implemented by lazy loading
        //      * forcing hibernate to fetch the contents by creating new ArrayList
        //      * without creating new ArrayList (roomPostDto.setRoomPhotos(roomPost.getRoomPhotos())), it will not work
        //      */
        //     roomPostDto.setRoomPhotos(new ArrayList<>(roomPost.getRoomPhotos()));
        // }

        for(RoomPost roomPost : listOfRoomPosts){
            listOfRoomPostListDtos.add(RoomPostMapper.mapToRoomPostListDto(roomPost));
        }
               
        roomPostsResponse.setAllRoomPosts(listOfRoomPostListDtos);
        roomPostsResponse.setPageNo(roomPosts.getNumber());
        roomPostsResponse.setPageSize(roomPosts.getSize());
        roomPostsResponse.setTotalContentSize(roomPosts.getTotalElements());
        roomPostsResponse.setLast(roomPosts.isLast());
        
        return roomPostsResponse;
    }

    

    @Override
    public List<RoomPostDto> getActiveRoomPostsByAgentId(Long agentId) {
        List<RoomPost> roomPosts = roomPostRepository.findActiveRoomPostsByAgentId(agentId);
        List<RoomPostDto> roomPostDtos = new ArrayList<>();
        for(RoomPost roomPost: roomPosts){
            roomPostDtos.add(RoomPostMapper.mapToRoomPostDto(roomPost));
        }
        return roomPostDtos;
    }

    @Override
    public List<RoomPostDto> getArchivedRoomPostsByAgentId(Long agentId) {
        List<RoomPost> roomPosts = roomPostRepository.findArchivedRoomPostsByAgentId(agentId);
        List<RoomPostDto> roomPostDtos = new ArrayList<>();
        for(RoomPost roomPost: roomPosts){
            roomPostDtos.add(RoomPostMapper.mapToRoomPostDto(roomPost));
        }
        return roomPostDtos;
    }

    @Override
    public RoomPostDto reactivateRoomPost(Long id, AgentDto currentAgent) {
        Optional<RoomPost> roomPost = roomPostRepository.findById(id);
        if(roomPost.isPresent()){
            RoomPost roomPostToActivate = roomPost.get();
            if(roomPostToActivate.getAgent().getUsername().equals(currentAgent.getUsername())){
                roomPostToActivate.setArchived(false);
                RoomPost updatedRoomPost = roomPostRepository.save(roomPostToActivate);
                return RoomPostMapper.mapToRoomPostDto(updatedRoomPost);
            }else{
                throw new EntityNotFoundException("RoomPost Not Found");
            }
        }else{
            throw new EntityNotFoundException("RoomPost Not Found");
        }
    }

    
}
