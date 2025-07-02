package com.newdev.inservice.interfaces;


import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public interface IAuthService {

    public Authentication authenticate(String username, String password);
}
