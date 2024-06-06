package com.thiha.roomrent.service;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.thiha.roomrent.dto.AgentDto;
import com.thiha.roomrent.dto.AgentRegisterDto;
import com.thiha.roomrent.enums.UserRole;
import com.thiha.roomrent.exceptions.S3ImageUploadException;
import com.thiha.roomrent.exceptions.EmailAlreadyRegisteredException;
import com.thiha.roomrent.exceptions.EntityNotFoundException;
import com.thiha.roomrent.exceptions.NameAlreadyExistedException;
import com.thiha.roomrent.exceptions.ProfileImageNotFoundException;
import com.thiha.roomrent.mapper.AgentMapper;
import com.thiha.roomrent.model.Agent;
import com.thiha.roomrent.repository.AgentRepository;
import com.thiha.roomrent.service.AgentService;
import com.thiha.roomrent.service.impl.AgentServiceImpl;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AgentService implements AgentServiceImpl{
    private AgentRepository agentRepository;
    private PasswordEncoder passwordEncoder;
    private S3ImageService imageService;
    @Override
    public AgentDto createAgent(AgentRegisterDto registeredAgent) {
        Optional<Agent> agentByEmail = agentRepository.findByEmail(registeredAgent.getEmail());
        if(agentByEmail.isPresent()){
            throw new EmailAlreadyRegisteredException("Email already registered");
        }
        Optional<Agent> agentByName = agentRepository.findByUsername(registeredAgent.getUsername());
        if(agentByName.isPresent()){
            throw new NameAlreadyExistedException("Name already taken");
        }
        if (registeredAgent.getProfileImage() == null) {
            throw new ProfileImageNotFoundException("Profile Image cannot be null");
        }
        MultipartFile profileImage = registeredAgent.getProfileImage();
        try{
            imageService.uploadImage(profileImage.getOriginalFilename(), profileImage);
        }catch(IOException e){
            throw new S3ImageUploadException(e.getMessage());
        }
        registeredAgent.setProfilePhoto(profileImage.getOriginalFilename());
        registeredAgent.setRole(UserRole.AGENT);
        String hashedPassword = passwordEncoder.encode(registeredAgent.getPassword());
        
        registeredAgent.setPassword(hashedPassword);
        Agent agent = AgentMapper.mapToAgent(AgentMapper.mapToAgentDtoFromAgentRegisterDto(registeredAgent));
        // set the creation date
        agent.setCreatedAt(new Date());
        Agent savedAgent = agentRepository.save(agent);
        return AgentMapper.mapToAgentDto(savedAgent);
    }

    @Override
    public AgentDto findAgentByEmail(String email) {
        Optional<Agent> agent = agentRepository.findByEmail(email);
        if (agent.isPresent()) {
            return AgentMapper.mapToAgentDto(agent.get());
        }else{
            return null;
        }
    }

    @Override
    public AgentDto findAgentById(Long id) {
        Optional<Agent> agentOptional = agentRepository.findById(id);
        if ((agentOptional.isPresent())) {
            return AgentMapper.mapToAgentDto(agentOptional.get());
        }
        return null;
    }

    @Override
    public AgentDto findAgentByName(String name) {
        Optional<Agent> optionalAgent = agentRepository.findByUsername(name);
        if(optionalAgent.isPresent()){
            return AgentMapper.mapToAgentDto(optionalAgent.get());
        }
        throw new EntityNotFoundException("Agent cannot be found");
    }
    // @Override
    // public AgentDto updateExistingAgent(AgentDto newAgentDto, AgentDto existingAgentDto) {
    //     existingAgentDto.setPhoneNumber(newAgentDto.getPhoneNumber());
    //     existingAgentDto.setProfilePhoto(newAgentDto.getProfilePhoto());
    //     return AgentMapper.mapToAgentDto(agentRepository.save(AgentMapper.mapToAgent(existingAgentDto)));
    // }

    @Override
    public AgentDto updateExistingAgent(AgentRegisterDto newAgentDto, AgentDto existingAgentDto) {
        if(newAgentDto.getProfileImage()==null || newAgentDto.getProfileImage().isEmpty()){
            throw new ProfileImageNotFoundException("Profile photo cannnot be empty");
        }
        /*agent can only change phone number and profile picture
        delete the exsiting image on s3 and update the profile photo name in db 
        */ 
        MultipartFile newProfileImage = newAgentDto.getProfileImage();
        imageService.deleteImage(existingAgentDto.getProfilePhoto());
        String newFileName = newProfileImage.getOriginalFilename();
        try {
            imageService.uploadImage(newFileName, newProfileImage);
        } catch (IOException e) {
            throw new S3ImageUploadException("Error uploading Image to s3");
        }

        //change profile photo
        existingAgentDto.setProfilePhoto(newFileName);

        //change phone number
        existingAgentDto.setPhoneNumber(newAgentDto.getPhoneNumber());
        
        return AgentMapper.mapToAgentDto(agentRepository.save(AgentMapper.mapToAgent(existingAgentDto)));
    }
   
}
