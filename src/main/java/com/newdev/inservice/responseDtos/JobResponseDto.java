package com.newdev.inservice.responseDtos;

import com.newdev.inservice.models.enums.JobStatus;

import java.time.LocalDateTime;

public record JobResponseDto(String id,
                             DemandResponseDto originalDemand,
                             SubClientDto client,
                             SubTaskerDto tasker,
                             LocalDateTime startedAt,
                             LocalDateTime finishedAt,
                             LocalDateTime updatedAt,
                             JobStatus status,
                             int clientRating,
                             String clientFeedback) {
}
