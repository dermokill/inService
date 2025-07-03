package com.newdev.inservice.services;


import com.newdev.inservice.config.JwtProvider;
import com.newdev.inservice.exceptions.ResourceNotFoundException;
import com.newdev.inservice.interfaces.IUserService;
import com.newdev.inservice.models.User;
import com.newdev.inservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {


    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getProfile(UserDetails userDetails) {
        return Optional.ofNullable(userDetails.getUsername())
                .map(userRepository::findByEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User profile not found"));
    }


    @Override
    public List<User> getAllUsers() {

        return userRepository.findAll();
    }
}
