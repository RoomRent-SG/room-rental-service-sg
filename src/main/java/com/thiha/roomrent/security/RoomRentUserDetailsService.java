package com.thiha.roomrent.security;


import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.thiha.roomrent.model.UserModel;
import com.thiha.roomrent.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class RoomRentUserDetailsService implements UserDetailsService{
    
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserModel user;
        Optional<UserModel> optionalUser = userRepository.findByUsername(username);
        log.info("USERNAME "+ optionalUser);
        if(optionalUser.isPresent()){
            user = optionalUser.get();
            return new UserDetailsImpl(user);
        }else{
            log.info("user not found");
            throw new UsernameNotFoundException(username);
        }
        
    }


    public UserModel getUserWithUsername(String username){
        Optional<UserModel> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isPresent()){
            return optionalUser.get();
        }
        System.out.println("user not found");
        return null;
    }

    // private Collection<GrantedAuthority> getAuthorities(UserModel user){
    //     List<GrantedAuthority> authorities = new ArrayList<>();
    //     authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));
    //     return authorities;
    // }
   
}
