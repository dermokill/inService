package com.newdev.inservice.requestDtos;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class RegisterTaskerDto {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Gender is required")
    private String gender;

    @NotBlank(message = "Birthdate is required")
    private String birthdate;

    @NotBlank(message = "CIN is required")
    private String cin;

    @NotBlank(message = "Phone number is required")
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Role is required")
    private String role;

    // Tasker-specific fields
    @NotBlank(message = "tasker Type is required")
    private String taskerType; // "INDIVIDUAL", "SHOP_OWNER", "ENTREPRISE"

    @NotBlank(message = "Skill is required")
    private String skill;

    @NotBlank(message = "tasker City is required")
    private String taskerCity;

    @NotBlank(message = "Area is required")
    private String taskerArea;

    @NotBlank(message = "Experience is required")
    private String experience;

    @NotNull(message = "job Number is required")
    private Integer jobNumber;



    // ShopOwner fields
    private String shopAddress;

    private Integer shopLicenceNumber;


    //Entreprise fields
    private String entrepriseAddress;

    private String entrepriseName;

    private Integer entrepriseLicenceNumber;

    private Integer employeeNumber;
}
