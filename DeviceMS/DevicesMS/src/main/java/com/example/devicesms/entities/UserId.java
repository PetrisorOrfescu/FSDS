package com.example.devicesms.entities;




import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class UserId {

    @Id
    private UUID id;

    public UserId(UUID id) {
        this.id = id;
    }

    public UserId() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

}
