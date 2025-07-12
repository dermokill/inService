package com.newdev.inservice.controller;


import com.newdev.inservice.config.JwtProvider;
import com.newdev.inservice.exceptions.BadRequestException;
import com.newdev.inservice.exceptions.IllegalArgException;
import com.newdev.inservice.models.ImageTest;
import com.newdev.inservice.repository.imageRepository;
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
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/auth")
@Validated
public class AuthController {


    private final IAuthService authService;

    private final UserRepository userRepository;

    private final UserService userService;

    private final imageRepository imageRepository;


    @Autowired
    public AuthController(AuthService authService
            , UserRepository userRepository
            , UserService userService, imageRepository imageRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.userService = userService;
        this.imageRepository = imageRepository;
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

    @PostMapping("tasker")
    public ResponseEntity<?> testing(@RequestParam("images") @NotNull(message = "images are required") List<MultipartFile> images,
                                     @RequestParam @NotBlank(message = "First name is required") String firstName,
                                     @RequestParam @NotBlank(message = "Last name is required") String lastName,
                                     @RequestParam("image") MultipartFile file
    ) throws Exception {

        System.out.println("IM HERE");
        for(MultipartFile image : images){

        if(image == null || image.isEmpty())
            throw new BadRequestException("Image is required");

        String path = userService.insertImageByUserName(firstName,lastName,image);
        ImageTest imageTest = new ImageTest(path);
        imageRepository.save(imageTest);
    }
        return ResponseEntity.status(HttpStatus.CREATED).body(new JsonResponse("it worked"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login (@Valid @RequestBody LoginRequest loginRequest) {

        try {
            String username = loginRequest.getEmail();
            String password = loginRequest.getPassword();

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
