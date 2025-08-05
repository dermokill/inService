package com.newdev.inservice.controller;


import com.newdev.inservice.exceptions.BadRequestException;
import com.newdev.inservice.requestDtos.ImagesDto;
import com.newdev.inservice.requestDtos.RoleDto;
import com.newdev.inservice.serviceInterfaces.IUserService;
import com.newdev.inservice.models.User;
import com.newdev.inservice.models.enums.RoleEnum;
import com.newdev.inservice.repository.UserRepository;
import com.newdev.inservice.responseDtos.JsonResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@Validated
public class UserController {

    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;

    }

    @GetMapping("/test")
    public ResponseEntity<JsonResponse> test (@RequestHeader("Authorization") String jwt){

        return new ResponseEntity<>(new JsonResponse("welcome to hell mfs"), HttpStatus.OK);
    }

    // use @AuthenticationPrincipal to get all the loggedIn user info needed
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@AuthenticationPrincipal UserDetails userDetails){

        Object user = userService.getProfile(userDetails);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }



    @PutMapping(value = "insert-images",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> insertImagesTasker (@AuthenticationPrincipal UserDetails userDetails,
                                                 @Valid @ModelAttribute ImagesDto imagesDto) throws Exception
    {
        userService.insertTaskerImages(userDetails,imagesDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }



//    @GetMapping("/clients")  // testing
//    public ResponseEntity<?> getClients(
//            @RequestBody RoleDto dto) {
//
//
//        List<User> users = (dto.role() != null)
//                ? userRepository.getUsersByRole(RoleEnum.valueOf(dto.role().toUpperCase()))
//                : userRepository.findAll();
//
//        return new ResponseEntity<>(users, HttpStatus.OK);
//    }
}
