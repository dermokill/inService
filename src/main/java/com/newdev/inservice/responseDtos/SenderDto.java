package com.newdev.inservice.responseDtos;

import com.newdev.inservice.models.enums.RoleEnum;

public record SenderDto(String senderId,
                        String senderName,
                        RoleEnum senderRole) {}
