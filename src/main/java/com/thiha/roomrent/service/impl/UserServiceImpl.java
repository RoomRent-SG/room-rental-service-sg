package com.thiha.roomrent.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thiha.roomrent.model.Admin;
import com.thiha.roomrent.model.Agent;
import com.thiha.roomrent.model.UserModel;
import com.thiha.roomrent.repository.AdminRepository;
import com.thiha.roomrent.repository.AgentRepository;
import com.thiha.roomrent.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    AgentRepository agentRepository;

    @Autowired
    AdminRepository adminRepository;

    @Override
    public UserModel getUser(long id) {
        Optional<UserModel> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            UserModel user = optionalUser.get();
            if(user instanceof Agent){
                return (Agent) user;
            }else if(user instanceof Admin){
                return (Admin) user;
            }
            return user;
        }
        return null;
    }
    
}
