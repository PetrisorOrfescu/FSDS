package com.example.a1.controllers;

import com.example.a1.dtos.UserDTO;
import com.example.a1.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin
@RequestMapping(value = "/user")
public class UserController {
    private final UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UUID> insertUser(@RequestBody UserDTO userDTO) {
        UUID userId = userService.insert(userDTO);
        return new ResponseEntity<>(userId,HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<UserDTO> dtos = userService.findUsers();
        for (UserDTO userDTO : dtos) {
            Link userLink = linkTo(methodOn(UserController.class).getUser(userDTO.getId())).withRel("user");
            userDTO.add(userLink);
        }

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("id") UUID id) {
        UserDTO dto = userService.findUserById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);

    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("id")UUID id, @RequestBody UserDTO userDTO ){
        UUID newId = userService.update(id,userDTO);
        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UUID> deleteUser(@PathVariable("id") UUID id){
        UUID userid = userService.delete(id);
        return new ResponseEntity<>(userid,HttpStatus.OK);
    }


    @GetMapping(value = "/{username}/{password}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("username") String username,@PathVariable("password") String password){
        UserDTO user = userService.findUserByUsernameAndPassword(username,password);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

}
