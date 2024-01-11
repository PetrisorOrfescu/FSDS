package com.example.devicesms.controllers;

import com.example.devicesms.dtos.UserIdDTO;
import com.example.devicesms.services.UserIdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@CrossOrigin
@RequestMapping(value = {"/maps"})
public class UserIdController {
    private final UserIdService userIdService;
    public static final Logger LOGGER = LoggerFactory.getLogger(UserIdService.class);

    @Autowired
    public UserIdController(UserIdService userIdService) {
        this.userIdService = userIdService;
    }

    @PostMapping
    public ResponseEntity<UUID> insertMapping(@RequestBody UserIdDTO userIdDTO) {
        UUID userId = userIdService.insert(userIdDTO);
        return new ResponseEntity<>(userId, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<UserIdDTO>> getMappings() {
        List<UserIdDTO> dtos = userIdService.findMappings();
        for (UserIdDTO dto : dtos) {
            Link link = linkTo(methodOn(UserIdController.class).getMapping(dto.getId())).withRel("userid");
            dto.add(link);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserIdDTO> getMapping(@PathVariable("id") UUID id) {
        UserIdDTO dto = userIdService.findMappingByID(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UUID> deleteUserId(@PathVariable("id") UUID id) {
        UUID userid = userIdService.delete(id);
        return new ResponseEntity<>(userid, HttpStatus.OK);
    }

}
