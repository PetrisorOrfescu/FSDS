package com.example.devicesms.dtos;



import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

public class AssignDeviceDTO extends RepresentationModel<AssignDeviceDTO> {
    private UUID deviceId;
    private UUID userId;

    public AssignDeviceDTO() {
    }

    public AssignDeviceDTO(UUID deviceId, UUID userId) {
        this.deviceId = deviceId;
        this.userId = userId;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
