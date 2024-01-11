package com.example.devicesms.dtos;

import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

public class DeviceManagementDTO extends RepresentationModel<DeviceManagementDTO> {
    private String method;
    private UUID deviceId;
    private double maxHourlyConsumption ;
    private UUID assignedTo;

    public DeviceManagementDTO(String method, UUID deviceId, double maxHourlyConsumption, UUID assignedTo) {
        this.method = method;
        this.deviceId = deviceId;
        this.maxHourlyConsumption = maxHourlyConsumption;
        this.assignedTo = assignedTo;
    }

    public DeviceManagementDTO() {
    }

    @Override
    public String toString() {
        return "DeviceManagementDTO{" +
                "method='" + method + '\'' +
                ", deviceId=" + deviceId +
                ", maxHourlyConsumption=" + maxHourlyConsumption +
                ", assignedTo=" + assignedTo +
                '}';
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public double getMaxHourlyConsumption() {
        return maxHourlyConsumption;
    }

    public void setMaxHourlyConsumption(double maxHourlyConsumption) {
        this.maxHourlyConsumption = maxHourlyConsumption;
    }

    public UUID getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(UUID assignedTo) {
        this.assignedTo = assignedTo;
    }
}
