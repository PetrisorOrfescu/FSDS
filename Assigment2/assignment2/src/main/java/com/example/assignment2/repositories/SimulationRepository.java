package com.example.assignment2.repositories;

import com.example.assignment2.entities.SimulationEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SimulationRepository extends JpaRepository<SimulationEntry, UUID> {

    List<SimulationEntry> findByDeviceId(UUID deviceId);

    void deleteByDeviceId(UUID deviceId);
}
