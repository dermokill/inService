package com.newdev.inservice.controller;


import com.newdev.inservice.config.JwtProvider;
import com.newdev.inservice.interfaces.IAuthService;
import com.newdev.inservice.models.User;
import com.newdev.inservice.models.enums.RoleEnum;
import com.newdev.inservice.repository.UserRepository;
import com.newdev.inservice.requestDtos.LoginRequest;
import com.newdev.inservice.responseDtos.AuthResponse;
import com.newdev.inservice.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@Validated
public class AuthController {


    private final IAuthService authService;
    private final UserRepository userRepository;

    @Autowired
    public AuthController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody LoginRequest loginRequest) {

        try {
            String username = loginRequest.email();
            String password = loginRequest.password();

            System.out.println("Login Successful");
            System.out.println(username+ " ------- " +password);

            Authentication authentication = authService.authenticate(username , password);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // trying out new stuff
            User user = userRepository.findByEmail(username);
            String role = user.getRole().toString().toUpperCase();

            String token = JwtProvider.generateToken(authentication, role);

            return new ResponseEntity<>(new AuthResponse(token), HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
