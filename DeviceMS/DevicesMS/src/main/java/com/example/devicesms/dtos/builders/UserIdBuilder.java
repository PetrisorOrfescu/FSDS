package com.example.devicesms.dtos.builders;

import com.example.devicesms.dtos.UserIdDTO;
import com.example.devicesms.entities.UserId;

public class UserIdBuilder {
    public UserIdBuilder() {
    }
    public static UserIdDTO toUserIdDto(UserId userId){
        return new UserIdDTO(userId.getId());
    }
    public static UserId toUserId(UserIdDTO userIdDTO){
        return new UserId(userIdDTO.getId());
    }
}
