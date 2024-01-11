package com.example.assignment2.controllers;

import com.example.assignment2.dtos.DeviceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.assignment2.services.DeviceService;

import java.util.UUID;

@RestController
@RequestMapping(value = "/devices")
@CrossOrigin("http://localhost:3000/")
public class DeviceController {
    private final DeviceService deviceService;
    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping
    public ResponseEntity<UUID> insertDevice(@RequestBody DeviceDTO deviceDTO) {
        UUID deviceId = deviceService.insert(deviceDTO);
        return new ResponseEntity<>(deviceId, HttpStatus.CREATED);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<DeviceDTO> getDevice(@PathVariable("id") UUID id) {
        DeviceDTO dto = deviceService.findById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);

    }
    @PutMapping(value = "/{id}")
    public ResponseEntity<DeviceDTO> updateDevice(@PathVariable("id")UUID id, @RequestBody DeviceDTO deviceDTO ){
        UUID newId = deviceService.update(id,deviceDTO);
        return new ResponseEntity<>(deviceDTO,HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UUID> deleteDevice(@PathVariable("id") UUID id){
        UUID deviceId = deviceService.delete(id);
        return new ResponseEntity<>(deviceId,HttpStatus.OK);
    }

    @GetMapping(value = "/start/{id}")
    public ResponseEntity<String> simulateDevice(@PathVariable UUID id){
        deviceService.startSimulation(id);
        return  ResponseEntity.ok("Simulation complete");
    }
}
