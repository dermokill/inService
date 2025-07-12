package com.newdev.inservice.models;


import com.newdev.inservice.models.enums.Gender;
import com.newdev.inservice.models.enums.RoleEnum;
import lombok.*;
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

@Document(collection = "users")
public class User {

    @Id
    private String id = UUID.randomUUID().toString(); // only works with constructor NOT with Builder

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
