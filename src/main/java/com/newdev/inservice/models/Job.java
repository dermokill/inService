package com.newdev.inservice.models;

import com.newdev.inservice.models.enums.JobStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString

@Document(collection = "jobs")
public class Job {

    @Id
    private String id = UUID.randomUUID().toString();

    @DBRef
    private Demand originalDemand;

    @DBRef
    private Client client;

    @DBRef
    private Tasker tasker;

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;

    private JobStatus status = JobStatus.IN_PROGRESS;

    private int clientRating;

    private String clientFeedback;
}
