package com.newdev.inservice.responseDtos;

import com.newdev.inservice.models.enums.Gender;
import com.newdev.inservice.models.enums.RoleEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AdminDto(String id,
                       String fName,
                       String lName,
                       Gender gender,
                       LocalDate birthdate,
                       String cin,
                       String phone,
                       String email,
                       RoleEnum role,
                       String profileImage,
                       LocalDateTime createdAt,
                       LocalDateTime updatedAt) {
}
