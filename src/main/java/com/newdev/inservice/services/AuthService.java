package com.newdev.inservice.services;


import com.newdev.inservice.interfaces.IAuthService;
import com.newdev.inservice.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService {


    private final CustomUserServiceImpl customUserService;

    private final PasswordEncoder passwordEncoder;

    public AuthService(PasswordEncoder passwordEncoder, CustomUserServiceImpl customUserService) {
        this.passwordEncoder = passwordEncoder;
        this.customUserService = customUserService;
    }

    //authenticate methode to check user and password
    @Override
    public Authentication authenticate(String username, String password) {

        UserDetails userDetails = customUserService.loadUserByUsername(username);

        System.out.println("Sign in userDetails - " +userDetails);

        if(userDetails == null) {
            System.out.println("Sign in UserDetails - null ");
            throw new BadCredentialsException("Invalid username or password");
        }

        if(!passwordEncoder.matches(password, userDetails.getPassword())) {
            System.out.println("sign in userDetails - password not match " +userDetails);
            throw new BadCredentialsException("Invalid username or password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null , userDetails.getAuthorities());

    }
}
