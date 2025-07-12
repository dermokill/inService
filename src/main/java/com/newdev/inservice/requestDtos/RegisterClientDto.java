package com.newdev.inservice.requestDtos;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class RegisterClientDto {

    // Common fields
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
    @Size(min = 10)
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Role is required")
    private String role;

    // Client-specific fields (optional)
    @NotBlank(message = "City is required")
    private String clientCity;

    @NotBlank(message = "Area is required")
    private String clientArea;

    @NotBlank(message = "personal Address is required")
    private String personalAddress;
}
