package com.newdev.inservice.controller;


import com.newdev.inservice.interfaces.IUserService;
import com.newdev.inservice.models.User;
import com.newdev.inservice.models.enums.RoleEnum;
import com.newdev.inservice.repository.UserRepository;
import com.newdev.inservice.responseDtos.JsonResponse;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Validated
public class UserController {

    private final UserRepository userRepository;

    private final IUserService userService;

    @Autowired
    public UserController(UserRepository userRepository, IUserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;

    }

    @GetMapping("/test")
    public ResponseEntity<JsonResponse> test (@RequestHeader("Authorization") String jwt){

        return new ResponseEntity<>(new JsonResponse("welcome to hell mfs"), HttpStatus.OK);
    }

    // use @AuthenticationPrincipal to get all the loggedIn user info needed
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@AuthenticationPrincipal UserDetails userDetails){

        User user = userService.getProfile(userDetails);
        user.setPassword("");

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {

        List<User> users = userService.getAllUsers();

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/clients")  // testing
    public ResponseEntity<?> getClients(
            @RequestParam("role") @NotBlank(message = "Role is required.") String role) {

        List<User> users = userRepository.getUsersByRole(RoleEnum.valueOf(role));

        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
