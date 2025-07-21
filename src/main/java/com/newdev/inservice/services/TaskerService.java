package com.newdev.inservice.services;



import com.newdev.inservice.exceptions.ResourceNotFoundException;
import com.newdev.inservice.models.*;

import com.newdev.inservice.repository.UserRepository;

import com.newdev.inservice.serviceInterfaces.ITaskerService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


@Service
public class TaskerService implements ITaskerService {


    private final UserRepository userRepository;


    @Autowired
    public TaskerService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getTasker(String id) {

        User tasker =  userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tasker profile not found"));
        tasker.setPassword("");

        return tasker;
    }


}
