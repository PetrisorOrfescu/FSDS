package com.example.assignment2.repositories;

import com.example.assignment2.entities.Devices;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Devices, UUID> {

}
