package com.newdev.inservice.models;


import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder

@Document(collection = "users")
public class User {

    @Id
    private UUID id = UUID.randomUUID(); // only works with constructor NOT with Builder

    private String fName;

    private String lName;

    private Gender gender;

    private LocalDate birthdate;

    private String cin;

    private String phone;

    private String email;

    private String password;

    private RoleEnum role;

    private String profileImage;

    private LocalDateTime createdAt =  LocalDateTime.now();

    private LocalDateTime updatedAt;

}
