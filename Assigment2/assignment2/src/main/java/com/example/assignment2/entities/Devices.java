package com.example.assignment2.entities;

import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.UUID;

@Entity
public class Devices implements Serializable {

    @Id
    private UUID deviceId;

    @Column(name = "maxConsumption",nullable=false)
    private double maxConsumption;
    @Column(name = "assignedTo",nullable=false)
    private UUID assignedTo;

    public Devices(UUID deviceId, double maxConsumption, UUID assignedTo) {
        this.deviceId = deviceId;
        this.maxConsumption = maxConsumption;
        this.assignedTo = assignedTo;
    }
    public Devices(){

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