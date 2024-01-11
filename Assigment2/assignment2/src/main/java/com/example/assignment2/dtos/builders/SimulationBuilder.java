package com.example.assignment2.dtos.builders;

import com.example.assignment2.dtos.SimulationDTO;
import com.example.assignment2.entities.SimulationEntry;

public class SimulationBuilder {
    public SimulationBuilder() {
    }
    public static SimulationDTO toSimulationDTO(SimulationEntry simulationEntry){
        return new SimulationDTO(simulationEntry.getId(),simulationEntry.getConsumption(),simulationEntry.getTimestamp(),simulationEntry.getDeviceId());
    }
    public static SimulationEntry toSimulationEntry(SimulationDTO simulationDTO){
        return new SimulationEntry(simulationDTO.getId(),simulationDTO.getConsumption(),simulationDTO.getTimestamp(),simulationDTO.getDeviceId());
    }
}
