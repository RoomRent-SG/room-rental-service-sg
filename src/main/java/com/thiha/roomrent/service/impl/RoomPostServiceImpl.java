package com.thiha.roomrent.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
import com.thiha.roomrent.enums.AirConTime;
import com.thiha.roomrent.enums.CookingAllowance;
import com.thiha.roomrent.enums.PropertyType;
import com.thiha.roomrent.enums.RoomType;
import com.thiha.roomrent.enums.SharePub;
import com.thiha.roomrent.exceptions.EntityNotFoundException;
import com.thiha.roomrent.exceptions.RoomPhotoNotFoundException;
import com.thiha.roomrent.exceptions.RoomPhotosExceedLimitException;
import com.thiha.roomrent.exceptions.S3ImageUploadException;
import com.thiha.roomrent.mapper.RoomPostMapper;
import com.thiha.roomrent.model.Agent;
import com.thiha.roomrent.model.Location;
import com.thiha.roomrent.model.RoomPhoto;
import com.thiha.roomrent.model.RoomPost;
import com.thiha.roomrent.repository.AgentRepository;
import com.thiha.roomrent.repository.LocationRepository;
import com.thiha.roomrent.repository.RoomPostRepository;
import com.thiha.roomrent.service.LocationService;
import com.thiha.roomrent.service.RoomPhotoService;
import com.thiha.roomrent.service.RoomPostService;
import com.thiha.roomrent.service.S3ImageService;
import com.thiha.roomrent.service.StationService;
import com.thiha.roomrent.specification.RoomPostSpecification;
import com.thiha.roomrent.utility.DateTimeHandler;


@Service
public class RoomPostServiceImpl implements RoomPostService{
    @Autowired
    private RoomPostRepository roomPostRepository;
    @Autowired
    private S3ImageService s3ImageService;
    @Autowired
    private RoomPhotoService roomPhotoService;
    @Autowired
    private AgentRepository agentRepository;
    @Autowired
    private StationService stationService;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private LocationService locationService;




    @Value("${aws.cloudFront}")
    private String cloudFrontUrl;

    @Override
    @CacheEvict(value = "all_room_posts", allEntries = true)
    public RoomPostDto createRoomPost(RoomPostRegisterDto roomPostRegisterDto, AgentDto agent) {
        RoomPost roomPost = new RoomPost();
        
        List<MultipartFile> roomPhotoFiles = roomPostRegisterDto.getRoomPhotoFiles();
        roomPostImageValidation(roomPhotoFiles);
        
        List<RoomPhoto> roomPhotos = uploadImagesToS3(roomPhotoFiles, roomPost);
        roomPost.setAddress(roomPostRegisterDto.getAddress());
        /*
         * set the first image as the thumbnail
         */
        Agent ownerAgent = getAgentById(agent.getId());
        setRoomPostAttributesFromDto(roomPost, roomPostRegisterDto, ownerAgent, roomPhotos);
        RoomPost savedRoomPost = roomPostRepository.save(roomPost);

        return RoomPostMapper.mapToRoomPostDto(savedRoomPost);
    }

    private Agent getAgentById(long id){
        Optional<Agent> optionalAgent = agentRepository.findById(id);
        if(optionalAgent.isPresent()){
            return optionalAgent.get();
        }else{
            throw new EntityNotFoundException("Agent not foud");
        }
    }

    private void setRoomPostAttributesFromDto(RoomPost roomPost,
                     RoomPostRegisterDto dto, 
                     Agent ownerAgent,
                     List<RoomPhoto> roomPhotos){
        validateStationName(dto.getStationName());
        Location location = validateLocation(dto.getLocation());
        
        roomPost.setLocation(location);
        roomPost.setThumbnailImage(roomPhotos.get(0).getImageUrl());
        roomPost.setAirConTime(getEnumFromString(AirConTime.class, dto.getAirConTime()));
        roomPost.setAllowVisitor(dto.isAllowVisitor());
        roomPost.setCookingAllowance(getEnumFromString(CookingAllowance.class, dto.getCookingAllowance()));
        roomPost.setPropertyType(getEnumFromString(PropertyType.class, dto.getPropertyType()));
        roomPost.setRoomType(getEnumFromString(RoomType.class, dto.getRoomType()));
        roomPost.setSharePub(getEnumFromString(SharePub.class, dto.getSharePub()));
        roomPost.setStationName(dto.getStationName());
        roomPost.setTotalPax(dto.getTotalPax());
        roomPost.setDescription(dto.getDescription());
        roomPost.setAgent(ownerAgent);
        roomPost.setPrice(dto.getPrice()); 
        roomPost.setPostedAt(DateTimeHandler.getUTCNow());
        roomPost.setArchived(false);
        roomPost.setRoomPhotos(roomPhotos);
    }

    private void validateStationName(String stationName){
        stationService.getStationByName(stationName);
    }
    private Location validateLocation(String location){
        Optional<Location> optionalLocation = locationRepository.findLocationByName(location);
        if(optionalLocation.isPresent()){
            return optionalLocation.get();
        }
        throw new EntityNotFoundException("Location not found");
    }

    List<RoomPhoto> uploadImagesToS3(List<MultipartFile> images, RoomPost ownerRoomPost){
        List<RoomPhoto> roomPhotos = new ArrayList<>();
        images.forEach(image -> {
            try{
                s3ImageService.uploadImage(image.getOriginalFilename(), image);
                RoomPhoto roomPhoto = new RoomPhoto();
                roomPhoto.setImageUrl(cloudFrontUrl+image.getOriginalFilename());
                roomPhoto.setRoomPost(ownerRoomPost);
                roomPhoto.setFilename(image.getOriginalFilename());
                roomPhotos.add(roomPhoto);
            }catch(IOException e){
                throw new S3ImageUploadException("Error uploading room photos to s3");
            }
        });
        return roomPhotos;
    }

    private void roomPostImageValidation(List<MultipartFile> imagFiles){
        if(imagFiles==null || imagFiles.size()==0){
            throw new RoomPhotoNotFoundException("You need to provide room photos to create room post");
        }

        if (imagFiles.size()>10) {
            throw new RoomPhotosExceedLimitException("Too many room photos");
        }
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

        RoomPost originalRoomPost = getRoomPostIfExist(postId);
        validateOwnership(originalRoomPost, agent);
        
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
                roomPhotoService.deleteRoomPhotoById(existingRoomPhotos.get(i).getId());
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
                    existingRoomPhotos.add(roomPhoto);
                } catch (IOException e) {
                    throw new S3ImageUploadException("Error uploading room photos to s3");
                }
            }
        }
        originalRoomPost.setThumbnailImage(existingRoomPhotos.get(0).getImageUrl());
        originalRoomPost.setRoomPhotos(existingRoomPhotos);

        validateStationName(updateRoomPost.getStationName());
        Location location = validateLocation(updateRoomPost.getLocation());

        originalRoomPost.setLocation(location);
        originalRoomPost.setStationName(updateRoomPost.getStationName());
        originalRoomPost.setPrice(updateRoomPost.getPrice());
        originalRoomPost.setRoomType(getEnumFromString(RoomType.class, updateRoomPost.getRoomType()));
        originalRoomPost.setTotalPax(updateRoomPost.getTotalPax());
        originalRoomPost.setCookingAllowance(getEnumFromString(CookingAllowance.class, updateRoomPost.getCookingAllowance()));
        originalRoomPost.setSharePub(getEnumFromString(SharePub.class, updateRoomPost.getSharePub()));
        originalRoomPost.setAirConTime(getEnumFromString(AirConTime.class, updateRoomPost.getAirConTime()));
        originalRoomPost.setAllowVisitor(updateRoomPost.isAllowVisitor());
        originalRoomPost.setPropertyType(getEnumFromString(PropertyType.class, updateRoomPost.getPropertyType()));
        originalRoomPost.setDescription(updateRoomPost.getDescription());

        
        RoomPost savedRoomPost = roomPostRepository.save(originalRoomPost);

        return RoomPostMapper.mapToRoomPostDto(savedRoomPost);
    }

    private RoomPost getRoomPostIfExist(long roomPostId){
        Optional<RoomPost> optionalRoomPost = roomPostRepository.findById(roomPostId);
        if(!optionalRoomPost.isPresent()){
            throw new EntityNotFoundException("RoomPost cannot be found");
        }
        return optionalRoomPost.get();
    }

    private void validateOwnership(RoomPost roomPost, AgentDto agent){
        if(!roomPost.getAgent().getUsername().equals(agent.getUsername())){
            //unauthorized entity
            throw new EntityNotFoundException("RoomPost cannot be found");
        }
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
    public AllRoomPostsResponse getAllActiveRoomPosts(int pageNo, int pageSize, Map<String, String> searchFilter) {
        
        /*
         * must add isArchive attribute to retrieve active room posts because specification api cannot work with custom query
         */
        RoomPostSearchFilter roomPostFilter = createSearchFilter(searchFilter);
        roomPostFilter.setArchived(false);
        RoomPostSpecification specification = new RoomPostSpecification(roomPostFilter);
        
        pageNo = pageNo<0? 0 : pageNo ;
        pageSize = pageSize<0? 10: pageSize;
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("postedAt").descending());
        Page<RoomPost> roomPosts = new PageImpl<>(new ArrayList<RoomPost>());
        List<RoomPost> listOfRoomPosts;

        try{
            roomPosts = roomPostRepository.findAll(specification, pageable);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        listOfRoomPosts = roomPosts.getContent();
        List<RoomPostListDto> listOfRoomPostListDtos = convertToRoomPostDtoList(listOfRoomPosts); 
               
        AllRoomPostsResponse allRoomPostsResponse = createRoomPostListResponse(listOfRoomPostListDtos, roomPosts);
        
        return allRoomPostsResponse;
    }

    // TODO: Change name of RoomPostListDto
    private List<RoomPostListDto> convertToRoomPostDtoList(List<RoomPost> roomPosts){
        List<RoomPostListDto> listOfRoomPostListDtos = new ArrayList<>();
        for(RoomPost roomPost : roomPosts){
            listOfRoomPostListDtos.add(RoomPostMapper.mapToRoomPostListDto(roomPost));
        }
        return listOfRoomPostListDtos;
    }

    private AllRoomPostsResponse createRoomPostListResponse(List<RoomPostListDto> roomPostList, 
                Page<RoomPost> roomPostPageResponse){
        AllRoomPostsResponse response = new AllRoomPostsResponse();
        response.setAllRoomPosts(roomPostList);
        response.setPageNo(roomPostPageResponse.getNumber());
        response.setPageSize(roomPostPageResponse.getSize());
        response.setTotalContentSize(roomPostPageResponse.getTotalElements());
        response.setLast(roomPostPageResponse.isLast());
        
        return response;
    }

    

    @Override
    public AllRoomPostsResponse getActiveRoomPostsByAgentId(Long agentId, int pageNo, int pageSize) {
        pageNo = pageNo <0 ? 0: pageNo;
        pageSize = pageSize < 0? 10: pageSize;
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("postedAt").descending());

        Page<RoomPost> activeRoomPostsPage = roomPostRepository.findActiveRoomPostsByAgentId(agentId, pageable);
        List<RoomPost> activeRoomPosts = activeRoomPostsPage.getContent();
        List<RoomPostListDto> activeRoomPostList = convertToRoomPostDtoList(activeRoomPosts);
        AllRoomPostsResponse response = createRoomPostListResponse(activeRoomPostList, activeRoomPostsPage);
        return response;
    }

    @Override
    public AllRoomPostsResponse getArchivedRoomPostsByAgentId(Long agentId, int pageNo, int pageSize) {
        pageNo = pageNo <0 ? 0: pageNo;
        pageSize = pageSize < 0? 10: pageSize;
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("postedAt").descending());

        Page<RoomPost> archivedRoomPostsPage = roomPostRepository.findArchivedRoomPostsByAgentId(agentId, pageable);
        List<RoomPost> archivedRoomPosts = archivedRoomPostsPage.getContent();
        List<RoomPostListDto> archivedRoomPostList = convertToRoomPostDtoList(archivedRoomPosts);
        AllRoomPostsResponse response = createRoomPostListResponse(archivedRoomPostList, archivedRoomPostsPage);
        
        return response;
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

    private RoomPostSearchFilter createSearchFilter(Map<String, String> filter){
        RoomPostSearchFilter searchFilter = new RoomPostSearchFilter();

        if (filter == null) {
            return new RoomPostSearchFilter();
        }

        for (String key: filter.keySet()){
            String value = filter.get(key);
            switch (key) {
                case "airConTime":
                    searchFilter.setAirConTime(getEnumFromString(AirConTime.class, value));
                    break;
                case "cookingAllowance":
                    searchFilter.setCookingAllowance(getEnumFromString(CookingAllowance.class, value));
                    break;
                case "propertyType":
                    searchFilter.setPropertyType(getEnumFromString(PropertyType.class, value));
                    break;
                case "roomType":
                    searchFilter.setRoomType(getEnumFromString(RoomType.class, value));
                    break;
                case "sharePub":
                    searchFilter.setSharePub(getEnumFromString(SharePub.class, value));
                    break;
                case "stationName":
                    searchFilter.setStationName(value);
                    break;
                case "minPrice":
                    searchFilter.setMinPrice(Double.valueOf(value));
                    break;
                case "maxPrice":
                    searchFilter.setMaxPrice(Double.valueOf(value));
                    break;
            
                default:
                    break;
            }
        }

        return searchFilter;
    }

    // TODO check lowercase string
    private <T extends Enum<T>> T getEnumFromString(Class<T> enumClass, String value){
        if(enumClass.isEnum()){
            for(T enumConstant: enumClass.getEnumConstants()){
                if (enumConstant.toString().equals(value)) {
                    return enumConstant;
                }
            }
        }
        return null;
    }

    @Override
    public Map<String, Object> getRoomPostRegisterMetadata() {
        Map<String, Object> metaData = new HashMap<>();
        Set<String> stations = stationService.getAllStationNames();
        metaData.put("stationName", stations);

        Set<String> locations = locationService.getAllLocations().stream().map(location-> location.getName()).collect(Collectors.toSet());
        metaData.put("location", locations);
        
        metaData.put("address", "required");
        metaData.put("price", "required");
        metaData.put("roomType", RoomType.getValueList());
        metaData.put("totalPax", "required");
        metaData.put("cookingAllowance", CookingAllowance.getValueList());
        metaData.put("sharePub", SharePub.getValueList());
        metaData.put("airConTime", AirConTime.getValueList());
        metaData.put("allowVisitor", "boolean");
        metaData.put("propertyType", PropertyType.getValueList());
        metaData.put("description", "room post description");
        metaData.put("roomPhotoFiles", "Image File array");
        return metaData;
    }


    @Override
    public RoomPostDto archiveRoomPostById(Long roomPostId, Long agentId) {
       Optional<RoomPost> optioalRoomPost = roomPostRepository.findById(roomPostId);
       if(!optioalRoomPost.isPresent()){
            throw new EntityNotFoundException("Room post could not be found");
       }
       RoomPost roomPost = optioalRoomPost.get();
       if(roomPost.getAgent().getId() != agentId){
            throw new EntityNotFoundException("Room post could not be found");
       }
       roomPost.setArchived(true);
       RoomPost archivedRoomPost = roomPostRepository.save(roomPost);
       return RoomPostMapper.mapToRoomPostDto(archivedRoomPost);
    }
    
}
