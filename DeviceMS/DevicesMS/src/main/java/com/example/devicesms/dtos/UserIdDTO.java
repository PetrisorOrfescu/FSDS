package com.example.devicesms.dtos;

import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

public class UserIdDTO extends RepresentationModel<UserIdDTO> {
    private UUID id;

    public UserIdDTO(UUID id) {
        this.id = id;
    }

    public UserIdDTO() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

}
