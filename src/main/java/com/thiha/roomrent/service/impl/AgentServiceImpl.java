package com.thiha.roomrent.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.thiha.roomrent.dto.AgentDto;
import com.thiha.roomrent.dto.AgentRegisterDto;
import com.thiha.roomrent.enums.UserRole;
import com.thiha.roomrent.event.OnRegisterationCompleteEvent;
import com.thiha.roomrent.exceptions.S3ImageUploadException;
import com.thiha.roomrent.exceptions.EmailAlreadyRegisteredException;
import com.thiha.roomrent.exceptions.EntityNotFoundException;
import com.thiha.roomrent.exceptions.InvalidRegistrationConfirmationTokenException;
import com.thiha.roomrent.exceptions.NameAlreadyExistedException;
import com.thiha.roomrent.exceptions.ProfileImageNotFoundException;
import com.thiha.roomrent.mapper.AgentMapper;
import com.thiha.roomrent.model.Agent;
import com.thiha.roomrent.model.ConfirmationToken;
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
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private S3ImageService imageService;

    @Value("${aws.cloudFront}")
    private String cloudFrontUrl;

    @Override
    public AgentDto createAgent(AgentRegisterDto registeredAgent) {
        Date funStart = DateTimeHandler.getUTCNow();
        System.out.println("Start of createAgent method");
        validateAgentRegistrationDetails(registeredAgent);

        MultipartFile profileImage = registeredAgent.getProfileImage();

        // TODO image upload is taking too long. Need to re-design
        uploadAgentProfileImage(profileImage);

        Agent newAgent = new Agent();
        ConfirmationToken confirmationToken = generateConfirmationToken();
        Date now = DateTimeHandler.getUTCNow();

        newAgent.setProfilePhoto(cloudFrontUrl+profileImage.getOriginalFilename());
        newAgent.setRole(UserRole.AGENT);
        newAgent.setConfirmationToken(confirmationToken);

        Date start = DateTimeHandler.getUTCNow(); 
        String hashedPassword = passwordEncoder.encode(registeredAgent.getPassword());
        Date end = DateTimeHandler.getUTCNow();
        Long diffInSec = (end.getTime() - start.getTime()) / 1000000;
        System.out.println("Password encoding lasts "+diffInSec);

        newAgent.setPassword(hashedPassword);

        newAgent.setUsername(registeredAgent.getUsername());
        newAgent.setEmail(registeredAgent.getEmail());
        newAgent.setPhoneNumber(registeredAgent.getPhoneNumber());
        newAgent.setEnabled(false);
        newAgent.setCreatedAt(now);
        
        confirmationToken.setAgent(newAgent);
        
        Date saveStart = DateTimeHandler.getUTCNow(); 
        Agent savedAgent = agentRepository.save(newAgent);
        Date saveEnd = DateTimeHandler.getUTCNow();
        Long saveTime = (saveEnd.getTime() - saveStart.getTime()) / 1000000;
        System.out.println("Password encoding lasts "+saveTime);

        eventPublisher.publishEvent(new OnRegisterationCompleteEvent(savedAgent, savedAgent.getUsername(), "has registered"));
        System.out.println("End of createAgent method");
        Date funEnd = DateTimeHandler.getUTCNow();
        Long totalSec = (funEnd.getTime() - funStart.getTime()) / 1000;
        System.out.println("Create lasts "+ totalSec);
        return AgentMapper.mapToAgentDto(savedAgent);
    }

    private ConfirmationToken generateConfirmationToken(){
        ConfirmationToken confirmationToken = new ConfirmationToken();
        Date now = DateTimeHandler.getUTCNow();
        confirmationToken.setCreatedAt(now);
        confirmationToken.setTokenValue(UUID.randomUUID());

        return confirmationToken;
    }



    private void validateAgentRegistrationDetails(AgentRegisterDto registeredAgent){
        System.out.println("validation running twice");
        checkAlreadyRegisteredEmail(registeredAgent.getEmail());
        checkAlreadyRegisteredName(registeredAgent.getUsername());
        checkProfileImageIsPresent(registeredAgent.getProfileImage());
    }

    // TODO combine query for email and username validation
    private void checkAlreadyRegisteredEmail(String email){
        Date start = DateTimeHandler.getUTCNow();
        Optional<Agent> agentByEmail = agentRepository.findByEmail(email);
        if(agentByEmail.isPresent()){
            throw new EmailAlreadyRegisteredException("Email already registered");
        }
        Date end = DateTimeHandler.getUTCNow();
        Long diffInSec = (end.getTime() - start.getTime()) / 1000;
        System.out.println("Email Check lasts "+diffInSec);
    }

    private void checkAlreadyRegisteredName(String name){
        Date start = DateTimeHandler.getUTCNow(); 
        Optional<Agent> agentByName = agentRepository.findByUsername(name);
        if(agentByName.isPresent()){
            throw new NameAlreadyExistedException("Name already taken");
        }
        Date end = DateTimeHandler.getUTCNow();
        Long diffInSec = (end.getTime() - start.getTime()) / 1000;
        System.out.println("Name Check lasts "+diffInSec);
    }

    private void checkProfileImageIsPresent(MultipartFile profileImage){
        if (profileImage == null || profileImage.isEmpty()) {
            throw new ProfileImageNotFoundException("Profile Image should not be empty");
        }
    }

    private void uploadAgentProfileImage(MultipartFile profileImage){
        Date start = DateTimeHandler.getUTCNow(); 
        try{
            imageService.uploadImage(profileImage.getOriginalFilename(), profileImage);
        }catch(IOException e){
            throw new S3ImageUploadException(e.getMessage());
        }
        Date end = DateTimeHandler.getUTCNow();
        Long diffInSec = (end.getTime() - start.getTime()) / 1000;
        System.out.println("Image upload lasts "+diffInSec);
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
    public AgentDto updateExistingAgent(AgentRegisterDto updatingAgent, Long agentId) {
        if(updatingAgent.getProfileImage()==null || updatingAgent.getProfileImage().isEmpty()){
            throw new ProfileImageNotFoundException("Profile photo cannnot be empty");
        }

        Optional<Agent> optionalAgent = agentRepository.findById(agentId);
        if(!optionalAgent.isPresent()){
            throw new EntityNotFoundException("User not found");
        }
        Agent existingAgent = optionalAgent.get();

        /*agent can only change phone number and profile picture
        delete the exsiting image on s3 and update the profile photo name in db 
        */ 
        MultipartFile newProfileImage = updatingAgent.getProfileImage();
        imageService.deleteImage(existingAgent.getProfilePhoto());
        String newFileName = newProfileImage.getOriginalFilename();
        uploadAgentProfileImage(newProfileImage);

        //change profile photo
        existingAgent.setProfilePhoto(newFileName);

        //change phone number
        existingAgent.setPhoneNumber(updatingAgent.getPhoneNumber());
        
        return AgentMapper.mapToAgentDto(agentRepository.save(existingAgent));
    }

    @Override
    public List<AgentDto> findAllAgents() {
        List<Agent> agents = agentRepository.findAll();
        return agents.stream().map(agent -> AgentMapper.mapToAgentDto(agent)).collect(Collectors.toList());
    }

    @Override
    public AgentDto enableAgent(UUID token) {
        Optional<Agent> optionalAgent = agentRepository.findByConfirmationToken(token);
        if(!optionalAgent.isPresent()){
            throw new InvalidRegistrationConfirmationTokenException("Invalid confirmtaion token");
        }
        Agent agent = optionalAgent.get();
        agent.setEnabled(true);
        agentRepository.save(agent);
        return AgentMapper.mapToAgentDto(optionalAgent.get());
    }
   
}
