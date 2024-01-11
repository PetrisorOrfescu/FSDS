package com.example.devicesms.controllers;

import com.example.devicesms.dtos.DeviceDTO;
import com.example.devicesms.services.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin
@RequestMapping(value = "/device")
public class DeviceController {
    private final DeviceService deviceService;
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);
    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }
    @PostMapping
    public ResponseEntity<UUID> insertDevice(@RequestBody DeviceDTO deviceDTO) {
        UUID deviceId = deviceService.insert(deviceDTO);
        return new ResponseEntity<>(deviceId, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<DeviceDTO>> getDevices() {
        List<DeviceDTO> dtos = deviceService.findDevices();
        for (DeviceDTO deviceDTO : dtos) {
            Link userLink = linkTo(methodOn(DeviceController.class).getDevice(deviceDTO.getId())).withRel("device");
            deviceDTO.add(userLink);
        }

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DeviceDTO> getDevice(@PathVariable("id") UUID id) {
        DeviceDTO dto = deviceService.findDeviceById(id);
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

}
