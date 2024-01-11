package com.example.devicesms.controllers;

import com.example.devicesms.dtos.AssignDeviceDTO;
import com.example.devicesms.services.DeviceService;
import com.example.devicesms.services.UserIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController@CrossOrigin@RequestMapping(value = "/mapToDevice")
public class AssignationController {
    public final DeviceService  deviceService;
    public final UserIdService userIdService;


    @Autowired
    public AssignationController(DeviceService deviceService, UserIdService userIdService) {
        this.deviceService = deviceService;
        this.userIdService = userIdService;
    }

    @PostMapping
    public ResponseEntity<UUID> createMapping(@RequestBody AssignDeviceDTO assignDTO){
        UUID done = deviceService.updateAssignation(assignDTO);
        return new ResponseEntity<>(done, HttpStatus.OK);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UUID> deleteMapping(@PathVariable("id") UUID id){
        AssignDeviceDTO assignDeviceDTO = new AssignDeviceDTO(id, UUID.fromString("47c9fcda-3db3-4930-908c-0fc4c3ae3c49"));
        UUID done = deviceService.updateAssignation(assignDeviceDTO);
        return new ResponseEntity<>(done,HttpStatus.OK);
    }
}
