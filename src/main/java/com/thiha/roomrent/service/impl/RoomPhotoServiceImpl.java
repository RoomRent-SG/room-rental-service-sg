package com.thiha.roomrent.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thiha.roomrent.repository.RoomPhotoRepository;
import com.thiha.roomrent.service.RoomPhotoService;

@Service
public class RoomPhotoServiceImpl implements RoomPhotoService{

    @Autowired
    private RoomPhotoRepository roomPhotoRepository;

    @Override
    public void deleteRoomPhotoById(long id) {
        roomPhotoRepository.deleteById(id);
    }
    
}
