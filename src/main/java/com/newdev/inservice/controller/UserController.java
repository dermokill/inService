package com.newdev.inservice.controller;


import com.newdev.inservice.models.Client;
import com.newdev.inservice.models.User;
import com.newdev.inservice.repository.ClientRepository;
import com.newdev.inservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public UserController(UserRepository userRepository, ClientRepository clientRepository) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {

        List<User> users = userRepository.findAll();

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/clients")
    public ResponseEntity<List<Client>> getClients() {

        List<Client> users = clientRepository.findAll();

        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
