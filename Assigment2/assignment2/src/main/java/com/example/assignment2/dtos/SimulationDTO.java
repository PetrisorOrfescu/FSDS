package com.example.assignment2.dtos;

import org.antlr.v4.runtime.misc.NotNull;

import java.util.UUID;

public class SimulationDTO {

    private Integer id;
    private Double consumption;
    private String timestamp;
    private UUID deviceId;

    public SimulationDTO(Integer id, Double consumption, String timestamp, UUID deviceId) {
        this.id = id;
        this.consumption = consumption;
        this.timestamp = timestamp;
        this.deviceId = deviceId;
    }

    public SimulationDTO() {
    }

    @Override
    public String toString() {
        return "SimulationDTO{" +
                "id=" + id +
                ", consumption=" + consumption +
                ", timestamp='" + timestamp + '\'' +
                ", deviceId=" + deviceId +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getConsumption() {
        return consumption;
    }

    public void setConsumption(Double consumption) {
        this.consumption = consumption;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }
}
