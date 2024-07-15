package com.thiha.roomrent.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.thiha.roomrent.service.S3ImageService;
import com.thiha.roomrent.service.impl.AgentServiceImpl;
import com.thiha.roomrent.utility.DateTimeHandler;

@Service
public class AgentServiceImpl implements AgentService{
    @Autowired
    private AgentRepository agentRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private S3ImageService imageService;

    @Value("${aws.cloudFront}")
    private String cloudFrontUrl;

    @Override
    public AgentDto createAgent(AgentRegisterDto registeredAgent) {
        
        validateAgentRegistrationDetails(registeredAgent);

        MultipartFile profileImage = registeredAgent.getProfileImage();

        uploadAgentProfileImage(profileImage);
        
        registeredAgent.setProfilePhoto(cloudFrontUrl+profileImage.getOriginalFilename());
        registeredAgent.setRole(UserRole.AGENT);
        String hashedPassword = passwordEncoder.encode(registeredAgent.getPassword());
        registeredAgent.setPassword(hashedPassword);


        Agent agent = AgentMapper.mapToAgent(AgentMapper.mapToAgentDtoFromAgentRegisterDto(registeredAgent));
        // set the creation date
        agent.setCreatedAt(DateTimeHandler.getUTCNow());
        Agent savedAgent = agentRepository.save(agent);
        return AgentMapper.mapToAgentDto(savedAgent);
    }

    private void validateAgentRegistrationDetails(AgentRegisterDto registeredAgent){
        checkAlreadyRegisteredEmail(registeredAgent.getEmail());
        checkAlreadyRegisteredName(registeredAgent.getUsername());
        checkProfileImageIsPresent(registeredAgent.getProfileImage());
    }

    private void checkAlreadyRegisteredEmail(String email){
        Optional<Agent> agentByEmail = agentRepository.findByEmail(email);
        if(agentByEmail.isPresent()){
            throw new EmailAlreadyRegisteredException("Email already registered");
        }
    }

    private void checkAlreadyRegisteredName(String name){
        Optional<Agent> agentByName = agentRepository.findByUsername(name);
        if(agentByName.isPresent()){
            throw new NameAlreadyExistedException("Name already taken");
        }
    }

    private void checkProfileImageIsPresent(MultipartFile profileImage){
        if (profileImage == null || profileImage.isEmpty()) {
            throw new ProfileImageNotFoundException("Profile Image cannot be null");
        }
    }

    private void uploadAgentProfileImage(MultipartFile profileImage){
        try{
            imageService.uploadImage(profileImage.getOriginalFilename(), profileImage);
        }catch(IOException e){
            throw new S3ImageUploadException(e.getMessage());
        }
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
        uploadAgentProfileImage(newProfileImage);

        //change profile photo
        existingAgentDto.setProfilePhoto(newFileName);

        //change phone number
        existingAgentDto.setPhoneNumber(newAgentDto.getPhoneNumber());
        
        return AgentMapper.mapToAgentDto(agentRepository.save(AgentMapper.mapToAgent(existingAgentDto)));
    }

    @Override
    public List<AgentDto> findAllAgents() {
        List<Agent> agents = agentRepository.findAll();
        return agents.stream().map(agent -> AgentMapper.mapToAgentDto(agent)).collect(Collectors.toList());
    }
   
}
