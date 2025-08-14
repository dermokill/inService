package com.newdev.inservice.serviceInterfaces;


import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public interface IAuthService {

    Authentication authenticate(String username, String password);

    Authentication register(String username, String password);
}
