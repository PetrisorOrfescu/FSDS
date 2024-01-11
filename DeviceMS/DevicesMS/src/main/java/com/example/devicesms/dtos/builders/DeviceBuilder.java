package com.example.devicesms.dtos.builders;

import com.example.devicesms.dtos.DeviceDTO;
import com.example.devicesms.entities.Device;
import com.example.devicesms.entities.UserId;


public class DeviceBuilder {
    public DeviceBuilder() {
    }

    public static DeviceDTO toDeviceDTO(Device device){
        return new DeviceDTO(device.getId(),device.getDescription(),device.getAddress(),device.getMaxConsumption(), device.getUserId().getId());
    }

    public static Device toDevice(DeviceDTO deviceDTO){
        return new Device(deviceDTO.getId(),deviceDTO.getDescription(),deviceDTO.getAddress(),deviceDTO.getMaxConsumption(),new UserId(deviceDTO.getUserId()));
    }
}
