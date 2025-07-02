package com.newdev.inservice.interfaces;


import com.newdev.inservice.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IUserService {

    public User getProfile(String jwt);

    public List<User> getAllUsers();

}
