package com.example.devicesms.dtos;

import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

public class DeviceDTO extends RepresentationModel<DeviceDTO> {

    private UUID id;

    private String description;

    private String address;

    private String maxConsumption;

    private UUID userId;

    public DeviceDTO(UUID id, String description, String address, String maxConsumption, UUID userId) {
        this.id = id;
        this.description = description;
        this.address = address;
        this.maxConsumption = maxConsumption;
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMaxConsumption() {
        return maxConsumption;
    }

    public void setMaxConsumption(String maxConsumption) {
        this.maxConsumption = maxConsumption;
    }
}
