package com.thiha.roomrent.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.thiha.roomrent.dto.AllRoomPostsResponse;
import com.thiha.roomrent.dto.FilterKeywords;
import com.thiha.roomrent.dto.RoomPostDto;
import com.thiha.roomrent.dto.RoomPostSearchFilter;
import com.thiha.roomrent.service.RoomPostService;

@RestController
@RequestMapping("api/public")
public class PublicController {
    @Autowired
    private RoomPostService roomPostService;
    
    @GetMapping("/all-room-posts")
    public ResponseEntity<AllRoomPostsResponse> getAllroomPosts(
        @RequestParam(value = "pageNo", defaultValue = "1", required = false)int pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10", required = false ) int pageSize,
        @RequestParam Map<String, String> searchFilter
    ){
        /*
         * Page index will start from 1
         * overriding the 0-based index
         */
        AllRoomPostsResponse roomPostsResponse =  roomPostService.getAllActiveRoomPosts(pageNo-1, pageSize, searchFilter);
        return new ResponseEntity<AllRoomPostsResponse>(roomPostsResponse,HttpStatus.OK);
    }

    @GetMapping("/room-post/{id}")
    public ResponseEntity<RoomPostDto> getRoomPostDetails(
        @PathVariable Long id
    ){
        System.out.println("inside controller");
        RoomPostDto roomPostDto = roomPostService.findRoomPostById(id);
        return new ResponseEntity<>(roomPostDto, HttpStatus.OK);
    }

    @GetMapping("/filter-keywords")
    public ResponseEntity<FilterKeywords> getFilterKeywords(){
        FilterKeywords filterKeywords = new FilterKeywords();
        return new ResponseEntity<FilterKeywords>(filterKeywords, HttpStatus.OK);
    }
}
