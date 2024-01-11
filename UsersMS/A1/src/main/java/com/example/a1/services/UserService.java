package com.example.a1.services;


import com.example.a1.dtos.UserDTO;
import com.example.a1.dtos.builders.UserBuilder;
import com.example.a1.entities.Uuser;
import com.example.a1.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class UserService implements UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Uuser.class);
    private final UserRepository userRepository;

    @Value("${devices.service}")
    private String linkToDevicesService;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> findUsers() {
        List<Uuser> uuserList = userRepository.findAll();
        return uuserList.stream()
                .map(UserBuilder::toUserDTO)
                .collect(Collectors.toList());
    }

    public UserDTO findUserById(UUID id) {
        Optional<Uuser> uuser = userRepository.findById(id);
        if (!uuser.isPresent()) {
            LOGGER.error("User with id: {} is not present", id);
            throw new ResourceNotFoundException(Uuser.class.getSimpleName() + "With id: " + id);
        }
        return UserBuilder.toUserDTO(uuser.get());

    }

    public UUID insert(UserDTO userDTO) {
        Uuser user = UserBuilder.toEntity(userDTO);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        LOGGER.info("User with id {} was inserted in db", user.getId());
        user = userRepository.save(user);
        LOGGER.info("User with id {} was inserted in db", user.getId());

        try{
            URL url = new URL(linkToDevicesService + "/maps");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            // Enable input/output streams
            connection.setDoOutput(true);
            // Set the content type to JSON
            connection.setRequestProperty("Content-Type", "application/json");

            // Set the base authentication token
            connection.setRequestProperty("Authorization ", "Bearer " + "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTcwNDkxNDMxNCwiaWF0IjoxNzA0ODk2MzE0fQ.HvQbfWMZ-T9LfFB5qQX4VKMo-Y1zUKF4fXG0Be_aARNAk68O7skI8Cbn0pIuVVX7VdKWyiFjUVGfHto0IpXTvQ");

            // Create the JSON request body
            String requestBody = "{ \"id\": \""+user.getId()+"\" }";

            try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                outputStream.writeBytes(requestBody);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            // Do something with the response
           LOGGER.debug("Response from microservice: " + response.toString());
            // Close the connection
            connection.disconnect();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return user.getId();
    }

    public UUID delete(UUID id) {
        UserDTO user = findUserById(id);
        if (user == null) {
            LOGGER.error("User with id: {} is not present", id);
            throw new ResourceNotFoundException(Uuser.class.getSimpleName() + "With id: " + id);
        } else {
            try {
                // URL of the microservice you want to call
                URL url = new URL(linkToDevicesService + "/maps/" + user.getId());

                // Open a connection to the URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Set the request method to GET or POST, depending on your API
                connection.setRequestMethod("DELETE");

                // Set the base authentication token
                connection.setRequestProperty("Authorization ", "Bearer " + "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTcwNDkxNDMxNCwiaWF0IjoxNzA0ODk2MzE0fQ.HvQbfWMZ-T9LfFB5qQX4VKMo-Y1zUKF4fXG0Be_aARNAk68O7skI8Cbn0pIuVVX7VdKWyiFjUVGfHto0IpXTvQ");


                // Read the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                // Do something with the response
                System.out.println("Response from microservice: " + response.toString());

                // Close the connection
                connection.disconnect();

                userRepository.deleteById(id);
                LOGGER.info("User with id {} was deleted from db", user.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return user.getId();

    }

    public UUID update(UUID id, UserDTO userDTO) {
        UserDTO user = findUserById(id);
        if (user == null) {
            LOGGER.error("User with id: {} is not present", id);
            throw new ResourceNotFoundException(Uuser.class.getSimpleName() + "With id: " + id);
        } else {
            if (!userDTO.getFirstName().equals(""))
                user.setFirstName(userDTO.getFirstName());

            if (!userDTO.getLastName().equals(""))
                user.setLastName(userDTO.getLastName());

            if (!userDTO.getRole().equals(""))
                user.setRole(userDTO.getRole());

            if (!userDTO.getUsername().equals(""))
                user.setUsername(userDTO.getUsername());

            if (!userDTO.getPassword().equals("")) {
                PasswordEncoder passwordEncoder= new BCryptPasswordEncoder();
                user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            }

            userRepository.save(UserBuilder.toEntity(user));
        }
        return user.getId();
    }


    public UserDTO findUserByUsernameAndPassword(String userName, String passWord) {
        Optional<Uuser> user = userRepository.findUserByUsernameAndPassword(userName, passWord);
        if (!user.isPresent()) {
            LOGGER.error("User with username {} and password {} is not in the db", userName, passWord);
            throw new ResourceNotFoundException(Uuser.class.getSimpleName() + "with username: " + userName);
        }
        return UserBuilder.toUserDTO(user.get());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Uuser> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            LOGGER.error("User with username {}is not in the db", username);
            throw new ResourceNotFoundException(Uuser.class.getSimpleName() + "with username: " + username);
        }
        LOGGER.error("User with username {} and pass {}", user.get().getUsername(), user.get().getPassword());

        /*Uuser user = new Uuser();
        user.setFirstName("Mario");
        user.setLastName("Whatever");
        user.setRole("admin");
        user.setUsername("admin");
        user.setPassword(customPasswordEncoder.getPasswordEncoder().encode("1234"));
        user.setId(UUID.fromString("45774962-e6f7-41f6-b940-72ef63fa1943"));*/
        return user.get();
    }
}