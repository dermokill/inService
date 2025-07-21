package com.newdev.inservice.requestDtos;


import com.newdev.inservice.models.enums.TaskerType;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class DemandDto {

    @NonNull
    @NotBlank(message = "description is required")
    private String description;

    @NonNull
    @NotBlank(message = "demand location is required")
    private String location;

    @NonNull
    @NotBlank(message = "demand Time and Date is required")
    private String requestDate;

    @NonNull
    @NotBlank(message = "Initial Message is required")
    private String message;
}
