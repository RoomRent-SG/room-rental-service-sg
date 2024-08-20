package com.thiha.roomrent.security;


import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.thiha.roomrent.exceptions.EntityNotFoundException;
import com.thiha.roomrent.exceptions.UserDisabledException;
import com.thiha.roomrent.model.UserModel;
import com.thiha.roomrent.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RoomRentUserDetailsService implements UserDetailsService{
    
    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) {
        UserModel user;
        Optional<UserModel> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isPresent()){
            user = optionalUser.get();
            return new UserDetailsImpl(user);
        }else{
            throw new UsernameNotFoundException(username);
        }
        
    }

    public UserDetails loadUserWithUserID(Long id){
        Optional<UserModel> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            UserModel user = optionalUser.get();
            if(!user.isEnabled()){
                throw new UserDisabledException("User is disabled");
            }
            return new UserDetailsImpl(user);
        }
        throw new EntityNotFoundException("User not found");
    }

   
}
