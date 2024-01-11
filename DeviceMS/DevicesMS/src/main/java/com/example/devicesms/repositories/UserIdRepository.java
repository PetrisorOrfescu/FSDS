package com.example.devicesms.repositories;

import com.example.devicesms.entities.UserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserIdRepository extends JpaRepository<UserId, UUID> {

}
