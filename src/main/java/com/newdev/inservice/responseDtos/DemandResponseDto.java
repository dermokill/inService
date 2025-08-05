package com.newdev.inservice.responseDtos;

import com.newdev.inservice.models.enums.DemandStatus;
import com.newdev.inservice.models.enums.SkillType;

import java.time.LocalDateTime;
import java.util.List;

public record DemandResponseDto(String id,
                                SubClientDto subClientDto,
                                SubTaskerDto subTaskerDto,
                                LocalDateTime createdAt,
                                LocalDateTime updatedAt,
                                DemandStatus status,
                                SkillType taskType,
                                String description,
                                String location,
                                LocalDateTime requestDate,
                                List<MessageResponseDto> messages) {
}
