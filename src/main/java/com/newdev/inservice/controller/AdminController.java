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



@RestController
@RequestMapping("/api/v1/admin/users")
@Validated
public class AdminController {

    private final IUserService userService;

    public AdminController(IUserService userService) {
        this.userService = userService;
    }

    // ADMIN Only // validate tasker account
    @PutMapping("validate-tasker/{taskerId}")
    public ResponseEntity<?> validateTasker (@AuthenticationPrincipal UserDetails userDetails,
                                             @PathVariable String taskerId)
    {
        userService.validateTasker(userDetails,taskerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // ADMIN Only
    // get all clients, tasker, admins just have to specify role (ADMIN, CLIENT, TASKER)
    // returns paged list
    @GetMapping
    public ResponseEntity<PagedResponseDto<?>> getAllUsers(@AuthenticationPrincipal UserDetails userDetails,
                                                           @RequestBody RoleDto dto) {

        Page<?> users = userService.getAllUsers(userDetails, dto.getRole(), dto.getPage(), dto.getSize());
        return new ResponseEntity<>(new PagedResponseDto<>(users), HttpStatus.OK);
    }
}
