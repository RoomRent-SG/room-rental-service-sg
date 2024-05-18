package com.thiha.roomrent.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.thiha.roomrent.model.Admin;
import com.thiha.roomrent.model.Agent;
import com.thiha.roomrent.model.UserModel;
import com.thiha.roomrent.repository.AdminRepository;
import com.thiha.roomrent.repository.AgentRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RoomRentUserDetailsService implements UserDetailsService{
    
    private AgentRepository agentRepository;
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        System.out.println("Inside my custom service....");
        UserModel user;
        User userDetails;
        Optional<Admin> optionalAdmin = adminRepository.findByUsername(username);
        if(optionalAdmin.isPresent()){
            user = optionalAdmin.get();
            userDetails = new User(user.getUsername(), user.getPassword(), getAuthorities(user));
            return userDetails;
        }
        // search user in agent repository if the user cannot be found in admin repository
        Optional<Agent> optionalAgent = agentRepository.findByUsername(username);
        if(optionalAgent.isPresent()){
            user = optionalAgent.get();
            return new User(user.getUsername(), user.getPassword(), getAuthorities(user));
        }else{
            throw new UsernameNotFoundException(username);
        }
        
    }


    //to return the userId to the client
    public UserModel getUserWithUsername(String username){
        Optional<Admin> optionalAdmin = adminRepository.findByUsername(username);
        if(optionalAdmin.isPresent()){
            return optionalAdmin.get();
        }
        Optional<Agent> optionalAgent = agentRepository.findByUsername(username);
        if(optionalAgent.isPresent()){
            return optionalAgent.get();
        }
        return null;
    }

    private Collection<GrantedAuthority> getAuthorities(UserModel user){
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));
        return authorities;
    }
   
}
