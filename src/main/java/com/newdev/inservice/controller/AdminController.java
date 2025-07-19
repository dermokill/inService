package com.newdev.inservice.controller;


import com.newdev.inservice.models.User;
import com.newdev.inservice.requestDtos.RoleDto;
import com.newdev.inservice.responseDtos.PagedResponseDto;
import com.newdev.inservice.serviceInterfaces.IUserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/users")
@Validated
public class AdminController {

    private final IUserService userService;

    public AdminController(IUserService userService) {
        this.userService = userService;
    }

    @PutMapping("validate-tasker/{taskerId}") // ADMIN Only
    public ResponseEntity<?> validateTasker (@AuthenticationPrincipal UserDetails userDetails,
                                             @PathVariable String taskerId)
    {
        userService.validateTasker(userDetails,taskerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping // ADMIN Only
    public ResponseEntity<PagedResponseDto<User>> getClientsAndAdmins(@AuthenticationPrincipal UserDetails userDetails,
                                                          @RequestBody RoleDto dto) {

        System.out.println(dto.getSize() +" "+ dto.getPage());

        Page<User> users = userService.getClientsAndAdmins(userDetails,
                dto.getRole(), dto.getPage(), dto.getSize());

        for(User user : users.getContent()) {
            user.setPassword("");
        }
        return new ResponseEntity<>(new PagedResponseDto<>(users), HttpStatus.OK);
    }
}
