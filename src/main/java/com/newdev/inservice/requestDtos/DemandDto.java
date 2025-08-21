package com.newdev.inservice.requestDtos;


import com.newdev.inservice.models.enums.TaskerType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class DemandDto {

    @NotNull
    @NotBlank(message = "description is required")
    private String description;

    @NotNull
    @NotBlank(message = "demand location is required")
    private String location;

    @NotNull
    @NotBlank(message = "demand Time and Date is required")
    private String requestDate;

    @NotNull
    @NotBlank(message = "Initial Message is required")
    private String message;
}
