package com.newdev.inservice.responseDtos;

import com.newdev.inservice.models.enums.RoleEnum;

public record SubClientDto(String clientId,
                           String clientName,
                           RoleEnum clientRole) {
}
