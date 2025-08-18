package com.newdev.inservice.requestDtos;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class ClientReviewDto {

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    @NotNull
    private int clientRating;

    @Size(max = 500, message = "Feedback must not exceed 500 characters")
    @NotBlank(message = "feedback is required")
    private String clientFeedback;


}
