package com.newdev.inservice.requestDtos;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class MessageRequestDto {

    @NotBlank(message = "Message is required")
    private String message;
}
