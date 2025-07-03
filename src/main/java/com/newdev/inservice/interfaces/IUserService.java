package com.newdev.inservice.interfaces;


import com.newdev.inservice.models.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IUserService {

    public User getProfile(UserDetails  userDetails);

    public List<User> getAllUsers();

}
