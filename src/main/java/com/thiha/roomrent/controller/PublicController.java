package com.thiha.roomrent.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.thiha.roomrent.dto.AllRoomPostsResponse;
import com.thiha.roomrent.dto.RoomPostSearchFilter;
import com.thiha.roomrent.service.RoomPostService;

@RestController
@RequestMapping("api/public")
public class PublicController {
    @Autowired
    private RoomPostService roomPostService;
    
    @GetMapping("/all-room-posts")
    private ResponseEntity<AllRoomPostsResponse> getAllroomPosts(
        @RequestParam(value = "pageNo", defaultValue = "1", required = false)int pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10", required = false ) int pageSize,
        @RequestBody(required = false) RoomPostSearchFilter searchFilter
    ){
        /*
         * Page index will start from 1
         * overriding the 0-based index
         */
        AllRoomPostsResponse roomPostsResponse =  roomPostService.getAllRoomPosts(pageNo-1, pageSize, searchFilter);
        return new ResponseEntity<AllRoomPostsResponse>(roomPostsResponse,HttpStatus.OK);
    }
}
