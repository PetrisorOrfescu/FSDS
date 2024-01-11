package com.example.assignment2.dtos.builders;

import com.example.assignment2.dtos.DeviceDTO;
import com.example.assignment2.entities.Devices;

public class DeviceBuilder {

    public DeviceBuilder() {
    }

    public static DeviceDTO toDeviceDTO(Devices device){
        return new DeviceDTO(device.getDeviceId(),device.getMaxConsumption(),device.getAssignedTo());
    }
    public static Devices toDevice(DeviceDTO deviceDTO){
        return new Devices(deviceDTO.getDeviceId(), deviceDTO.getMaxConsumption(), deviceDTO.getAssignedTo());
    }
}
