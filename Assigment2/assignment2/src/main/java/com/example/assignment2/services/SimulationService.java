package com.example.assignment2.services;

import com.example.assignment2.dtos.SimulationDTO;
import com.example.assignment2.dtos.builders.SimulationBuilder;
import com.example.assignment2.entities.SimulationEntry;
import com.example.assignment2.repositories.SimulationRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
public class SimulationService {
    private static Logger LOGGER = LoggerFactory.getLogger(SimulationService.class);

    private final SimulationRepository simulationRepository;

    public SimulationService(SimulationRepository simulationRepository) {
        this.simulationRepository = simulationRepository;
    }
    public Integer insert(SimulationDTO simulationDTO) {
        SimulationEntry simulationEntry = SimulationBuilder.toSimulationEntry(simulationDTO);
        simulationEntry = simulationRepository.save(simulationEntry);
        LOGGER.debug("Saved");
        return simulationEntry.getId();
    }
    @Transactional
    public void deleteByDeviceId(UUID deviceId) {
        simulationRepository.deleteByDeviceId(deviceId);
    }
    @RabbitListener(queues = {"${rabbitmq.queue.json.name}"})
    public void consumeJsonSimulationData(SimulationDTO simulationDTO){
        //consuma mesaje din coada de simulare si insereaza in baza date + o sa verifice valoarea consumptions si partea cu sockets plm
        LOGGER.info(String.format("Consumed simulation entry " + simulationDTO.toString()));
        insert(simulationDTO);
    }
}
