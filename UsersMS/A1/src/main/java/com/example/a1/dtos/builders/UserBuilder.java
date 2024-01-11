package com.example.a1.dtos.builders;

import com.example.a1.dtos.UserDTO;
import com.example.a1.entities.Uuser;

public class UserBuilder {
    private UserBuilder() {

    }

    public static UserDTO toUserDTO(Uuser uuser) {
        return new UserDTO(uuser.getId(), uuser.getFirstName(), uuser.getLastName(), uuser.getRole(), uuser.getUsername(), uuser.getPassword());
    }

    public static Uuser toEntity(UserDTO userDTO) {
        return new Uuser(userDTO.getId(), userDTO.getFirstName(), userDTO.getLastName(),
                userDTO.getRole(), userDTO.getUsername(), userDTO.getPassword());
    }
}
