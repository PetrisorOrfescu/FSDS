package com.example.devicesms.services;

import com.example.devicesms.controllers.handlers.exceptions.ResourceNotFoundException;
import com.example.devicesms.dtos.UserIdDTO;
import com.example.devicesms.dtos.builders.UserIdBuilder;
import com.example.devicesms.entities.Device;
import com.example.devicesms.entities.UserId;
import com.example.devicesms.repositories.UserIdRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserIdService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Device.class);
    private final UserIdRepository userIdRepository;

    @Autowired
    public UserIdService(UserIdRepository userIdRepository) {
        this.userIdRepository = userIdRepository;
    }

    public List<UserIdDTO> findMappings(){
        List<UserId> mapList = userIdRepository.findAll();
        return mapList.stream().map(UserIdBuilder::toUserIdDto).collect(Collectors.toList());
    }

    public UserIdDTO findMappingByID(UUID id){
        Optional<UserId> mapping = userIdRepository.findById(id);
        if(!mapping.isPresent()){
            LOGGER.error("Mapping with id {} is not present", id);
            throw new ResourceNotFoundException(UserId.class.getSimpleName()+"With id:  "+id);
        }
        return UserIdBuilder.toUserIdDto(mapping.get());
    }

    public UUID insert(UserIdDTO userIdDTO){
        UserId mapping = UserIdBuilder.toUserId(userIdDTO);
        mapping = userIdRepository.save(mapping);
        LOGGER.debug("Inserted mapping with id {}", mapping.getId());
        return mapping.getId();
    }

    public UUID delete(UUID id){
        UserIdDTO dto = findMappingByID(id);
        if(dto == null){
            LOGGER.error("Mapping with id {} not found", id);
            throw new ResourceNotFoundException(UserId.class.getSimpleName()+"With id:  "+id);
        }
        else {
            userIdRepository.deleteById(id);
            LOGGER.debug("Deleted mapping with id {} ", id);
        }
        return id;
    }



}
