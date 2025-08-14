package com.newdev.inservice.controller;


import com.newdev.inservice.config.JwtProvider;

import com.newdev.inservice.requestDtos.RegisterClientDto;
import com.newdev.inservice.requestDtos.RegisterTaskerDto;
import com.newdev.inservice.responseDtos.JsonResponse;
import com.newdev.inservice.serviceInterfaces.IAuthService;
import com.newdev.inservice.models.User;
import com.newdev.inservice.repository.UserRepository;
import com.newdev.inservice.requestDtos.LoginRequest;
import com.newdev.inservice.responseDtos.AuthResponse;
import com.newdev.inservice.services.AuthService;
import com.newdev.inservice.services.UserService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/auth")
@Validated
public class AuthController {


    private final IAuthService authService;

    private final UserRepository userRepository;

    private final UserService userService;



    @Autowired
    public AuthController(AuthService authService
            , UserRepository userRepository
            , UserService userService) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.userService = userService;
    }


    @PostMapping("/register-client")
    public ResponseEntity<?> registerClient(@Valid @RequestBody RegisterClientDto dto){

            String token = userService.insertClient(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(token));
    }

    @PostMapping("/register-tasker")
    public ResponseEntity<?> registerTasker(@Valid @RequestBody RegisterTaskerDto dto) throws Exception {

        System.out.println("IM HERE"); // this is not reached

        String token = userService.insertTasker(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(token));
    }


    @PostMapping("/login")
    public ResponseEntity<?> login (@Valid @RequestBody LoginRequest loginRequest) {

        try {
            String username = loginRequest.getEmail();
            String password = loginRequest.getPassword();

            System.out.println("Login Successful");
            System.out.println(username+ " ------- " +password);

            Authentication authentication = authService.authenticate(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // trying out new stuff
            User user = userRepository.findByEmail(username);
            String role = user.getRole().toString().toUpperCase();

            String token = JwtProvider.generateToken(authentication, role);

            return new ResponseEntity<>(new AuthResponse(token), HttpStatus.OK);

        } catch (BadCredentialsException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JsonResponse("Invalid  email or password"));
        }
    }
}
