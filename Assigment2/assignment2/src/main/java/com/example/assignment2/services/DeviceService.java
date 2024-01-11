package com.example.assignment2.services;

import com.example.assignment2.dtos.DeviceDTO;
import com.example.assignment2.dtos.DeviceManagementDTO;
import com.example.assignment2.dtos.SimulationDTO;
import com.example.assignment2.dtos.builders.DeviceBuilder;
import com.example.assignment2.entities.Devices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.assignment2.repositories.DeviceRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Service
public class DeviceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Devices.class);

    private final DeviceRepository deviceRepository;

    private final SimulationService simulationService;

    private final WebSocketService webSocketService;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;
    @Value("${rabbitmq.routing.json.key}")
    private String routingJsonKey;
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository, SimulationService simulationService, WebSocketService webSocketService, RabbitTemplate rabbitTemplate) {
        this.deviceRepository = deviceRepository;
        this.simulationService = simulationService;
        this.webSocketService = webSocketService;
        this.rabbitTemplate = rabbitTemplate;
    }

    public UUID insert(DeviceDTO deviceDTO) {
        Devices devices = DeviceBuilder.toDevice(deviceDTO);
        devices = deviceRepository.save(devices);
        return devices.getDeviceId();
    }

    public DeviceDTO findById(UUID id) {
        Optional<Devices> device = deviceRepository.findById(id);
        if (!device.isPresent()) {
            LOGGER.error("Device not found");
        }
        return DeviceBuilder.toDeviceDTO(device.get());
    }

    public UUID delete(UUID id) {
        try{DeviceDTO deviceDTO = findById(id);
            if (deviceDTO == null) {
                LOGGER.error("Not found");
            } else {
                deviceRepository.deleteById(id);
                simulationService.deleteByDeviceId(id);
                LOGGER.debug("Deleted");
            }}catch(Exception e){
            LOGGER.error("Not found");
        }
        return id;
    }

    public UUID update(UUID id, DeviceDTO deviceDTO) {
        DeviceDTO device = findById(id);
        if (device == null) {
            LOGGER.error("Device with id: {} is not present", id);
        } else {
            device.setMaxConsumption(deviceDTO.getMaxConsumption());
            device.setAssignedTo(deviceDTO.getAssignedTo());
            simulationService.deleteByDeviceId(id);
            deviceRepository.save(DeviceBuilder.toDevice(deviceDTO));

        }
        assert device != null;
        return device.getDeviceId();
    }

    public void startSimulation(UUID deviceId) {
        DeviceDTO device = findById(deviceId);
        if (device != null) {

            SimulationDTO simulationDTO = new SimulationDTO();
            // Get the current timestamp
            Instant currentTimestamp = Instant.now();

            boolean passedMax = false;

            // Read sensor data from CSV file and print messages
            try (BufferedReader br = new BufferedReader(new FileReader("sensor.csv"))) {
            //try (BufferedReader br = new BufferedReader(new FileReader("sensor.csv"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    // Parse sensor value from the line
                    double sensorValue = Double.parseDouble(line);

                    if (sensorValue > device.getMaxConsumption())
                        passedMax = true;

                    // Display timestamp and sensor value
                    //displayTimestampAndSensorData(currentTimestamp, sensorValue);
                    LocalDateTime localDateTime = LocalDateTime.ofInstant(currentTimestamp, ZoneId.systemDefault());

                    // Format and display the timestamp and sensor value
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedTimestamp = localDateTime.format(formatter);

                    //LOGGER.info(deviceId + " " + formattedTimestamp + " " + sensorValue + "\n");

                    simulationDTO.setDeviceId(deviceId);
                    simulationDTO.setConsumption(sensorValue);
                    simulationDTO.setTimestamp(formattedTimestamp);

                    LOGGER.info(String.format("JSON MESSAGE SENT " + simulationDTO.toString()));
                    rabbitTemplate.convertAndSend(exchange, routingJsonKey, simulationDTO);

                    // Decrement timestamp by one hour
                    currentTimestamp = currentTimestamp.minusSeconds(3600);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (passedMax) {
                //send notification message
                LOGGER.info(String.format("passed max value"));
                String message = "Device with id: " + device.getDeviceId() + " exceeded the consumption limit\n";
                webSocketService.sendToClient(message);
            }

        }


    }

    @RabbitListener(queues = {"${rabbitmq.queue.name}"})
    public void consumeDeviceManagementData(DeviceManagementDTO deviceManagementDTO) {
        LOGGER.info(deviceManagementDTO.toString());

        DeviceDTO deviceDTO = new DeviceDTO(deviceManagementDTO.getDeviceId(), deviceManagementDTO.getMaxHourlyConsumption(), deviceManagementDTO.getAssignedTo());

        switch (deviceManagementDTO.getMethod()) {
            case "POST" -> insert(deviceDTO);
            case "DELETE" -> delete(deviceDTO.getDeviceId());
            case "PUT" -> update(deviceDTO.getDeviceId(), deviceDTO);
        }
    }


}
