package com.example.assignment2.dtos;
import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

public class DeviceDTO extends RepresentationModel<DeviceDTO>{
    private UUID deviceId;
    private double maxConsumption;
    private UUID assignedTo;

    public DeviceDTO(UUID deviceId, double maxConsumption, UUID assignedTo) {
        this.deviceId = deviceId;
        this.maxConsumption = maxConsumption;
        this.assignedTo = assignedTo;
    }

    public DeviceDTO() {
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public double getMaxConsumption() {
        return maxConsumption;
    }

    public void setMaxConsumption(double maxConsumption) {
        this.maxConsumption = maxConsumption;
    }

    public UUID getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(UUID assignedTo) {
        this.assignedTo = assignedTo;
    }
}
