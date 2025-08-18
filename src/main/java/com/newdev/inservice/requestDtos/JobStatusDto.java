package com.newdev.inservice.requestDtos;

import com.newdev.inservice.models.enums.JobStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter


public class JobStatusDto {

    @NotNull(message = "job status is required")
    private JobStatus jobStatus; // COMPLETED , CANCELED
}
