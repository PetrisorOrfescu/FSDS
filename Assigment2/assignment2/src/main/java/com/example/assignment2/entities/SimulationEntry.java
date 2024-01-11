package com.example.assignment2.entities;

import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.UUID;


@Entity
public class SimulationEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "consumption", nullable = false)
    private Double consumption;
    @Column(name = "timestamp", nullable = false)
    private String timestamp;
    @Column(name = "deviceId", nullable = false)
    private UUID deviceId;


// Constructors, getters, and setters

    public SimulationEntry(Integer id, Double consumption, String timestamp, UUID deviceId) {
        this.id = id;
        this.consumption = consumption;
        this.timestamp = timestamp;
        this.deviceId = deviceId;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public SimulationEntry() {
        // Default constructor required by JPA
    }


    // Getter and Setter methods

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
}