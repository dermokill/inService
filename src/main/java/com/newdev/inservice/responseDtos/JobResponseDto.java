package com.newdev.inservice.responseDtos;

import com.newdev.inservice.models.enums.JobStatus;

import java.time.LocalDateTime;

public record JobResponseDto(String id,
                             DemandResponseDto originalDemand,
                             SubClientDto client,
                             SubTaskerDto tasker,
                             LocalDateTime startedAt,
                             LocalDateTime finishedAt,
                             JobStatus status,
                             int clientRating,
                             String clientFeedback) {
}
