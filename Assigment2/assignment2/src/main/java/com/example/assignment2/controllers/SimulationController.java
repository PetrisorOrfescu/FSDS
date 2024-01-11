package com.example.assignment2.controllers;

import com.example.assignment2.dtos.SimulationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.assignment2.services.SimulationService;

import java.util.UUID;

@RestController
@RequestMapping(value = "/simulations")
public class SimulationController {

    private final SimulationService simulationService;
    @Autowired
    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @PostMapping
    public ResponseEntity<UUID> insertSimulation(@RequestBody SimulationDTO simulationDTO) {
        Integer id = simulationService.insert(simulationDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UUID> deleteSimulationData(@PathVariable("id") UUID id){
        simulationService.deleteByDeviceId(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
