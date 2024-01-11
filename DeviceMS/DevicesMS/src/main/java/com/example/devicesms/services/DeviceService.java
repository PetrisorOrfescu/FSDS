package com.example.devicesms.services;

import com.example.devicesms.controllers.handlers.exceptions.ResourceNotFoundException;
import com.example.devicesms.dtos.AssignDeviceDTO;
import com.example.devicesms.dtos.DeviceDTO;
import com.example.devicesms.dtos.DeviceManagementDTO;
import com.example.devicesms.dtos.builders.DeviceBuilder;
import com.example.devicesms.entities.Device;
import com.example.devicesms.repositories.DeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeviceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(Device.class);

    private final DeviceRepository deviceRepository;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;
    @Value("${rabbitmq.routing.key}")
    private String routingJsonKey;
    private RabbitTemplate rabbitTemplate;
    @Autowired
    public DeviceService(DeviceRepository deviceRepository, RabbitTemplate rabbitTemplate) {
        this.deviceRepository = deviceRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public List<DeviceDTO> findDevices() {
        List<Device> deviceList = deviceRepository.findAll();
        return deviceList.stream()
                .map(DeviceBuilder::toDeviceDTO)
                .collect(Collectors.toList());
    }

    public DeviceDTO findDeviceById(UUID id) {
        Optional<Device> device = deviceRepository.findById(id);
        if (!device.isPresent()) {
            LOGGER.error("Device with id: {} is not present", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + "With id: " + id);
        }
        return DeviceBuilder.toDeviceDTO(device.get());

    }

    public UUID insert(DeviceDTO deviceDTO) {
        deviceDTO.setUserId(UUID.fromString("47c9fcda-3db3-4930-908c-0fc4c3ae3c49"));
        Device device = DeviceBuilder.toDevice(deviceDTO);
        device = deviceRepository.save(device);
        LOGGER.debug("Device with id {} was inserted in db", device.getId());

        String[] parts = device.getMaxConsumption().split("K");

        // Extract the numeric value as a string
        String numericValueString = parts[0];

        // Convert the string to a double
        double numericValue = Double.parseDouble(numericValueString);

        DeviceManagementDTO deviceManagementDTO = new DeviceManagementDTO("POST",device.getId(),numericValue,deviceDTO.getUserId());
        rabbitTemplate.convertAndSend(exchange,routingJsonKey,deviceManagementDTO);

        return device.getId();
    }

    public UUID delete(UUID id) {
        DeviceDTO device = findDeviceById(id);
        if (device == null) {
            LOGGER.error("Device with id: {} is not present", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + "With id: " + id);
        } else {
            deviceRepository.deleteById(id);
            LOGGER.debug("Device with id {} was inserted in db", device.getId());

            DeviceManagementDTO deviceManagementDTO = new DeviceManagementDTO("DELETE",id,0,null);
            rabbitTemplate.convertAndSend(exchange,routingJsonKey,deviceManagementDTO);
        }
        return device.getId();

    }

    public UUID update(UUID id, DeviceDTO deviceDTO){
        DeviceDTO device = findDeviceById(id);
        if (device == null) {
            LOGGER.error("Device with id: {} is not present", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + "With id: " + id);
        } else {
            device.setDescription(deviceDTO.getDescription());
            device.setAddress(deviceDTO.getAddress());
            device.setMaxConsumption(deviceDTO.getMaxConsumption());
            deviceDTO.setUserId(device.getUserId());
            deviceRepository.save(DeviceBuilder.toDevice(deviceDTO));

            String[] parts = device.getMaxConsumption().split("K");

            // Extract the numeric value as a string
            String numericValueString = parts[0];

            // Convert the string to a double
            double numericValue = Double.parseDouble(numericValueString);

            DeviceManagementDTO deviceManagementDTO = new DeviceManagementDTO("PUT",id,numericValue,device.getUserId());
            rabbitTemplate.convertAndSend(exchange,routingJsonKey,deviceManagementDTO);
        }
        return device.getId();
    }

    public UUID updateAssignation(AssignDeviceDTO assignDeviceDTO){
        DeviceDTO deviceDTO = findDeviceById(assignDeviceDTO.getDeviceId());
        if (deviceDTO == null) {
            LOGGER.error("Device with id: {} is not present", assignDeviceDTO.getDeviceId());
            throw new ResourceNotFoundException(Device.class.getSimpleName() + "With id: " + assignDeviceDTO.getDeviceId());
        }
        else {
            deviceDTO.setUserId(assignDeviceDTO.getUserId());

            deviceRepository.save(DeviceBuilder.toDevice(deviceDTO));

            String[] parts = deviceDTO.getMaxConsumption().split("K");

            // Extract the numeric value as a string
            String numericValueString = parts[0];

            // Convert the string to a double
            double numericValue = Double.parseDouble(numericValueString);

            DeviceManagementDTO deviceManagementDTO = new DeviceManagementDTO("PUT",assignDeviceDTO.getDeviceId(),numericValue,assignDeviceDTO.getUserId());
            rabbitTemplate.convertAndSend(exchange,routingJsonKey,deviceManagementDTO);
        }
        return deviceDTO.getId();
    }


}
