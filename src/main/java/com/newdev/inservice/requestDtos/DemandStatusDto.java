package com.newdev.inservice.requestDtos;

import com.newdev.inservice.models.enums.DemandStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter


public class DemandStatusDto {

    @NotNull(message = "Demand status is required")
    private DemandStatus demandStatus; // ACCEPTED , REFUSED
}
