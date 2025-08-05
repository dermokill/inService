package com.newdev.inservice.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.newdev.inservice.models.enums.DemandStatus;
import com.newdev.inservice.models.enums.SkillType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString

@Document(collection = "demands")
public class Demand {

    @Id
    private String id =  UUID.randomUUID().toString();

    @DBRef
    private Client client;

    @DBRef
    private Tasker tasker;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    private DemandStatus status =  DemandStatus.PENDING;

    private SkillType taskType;

    private String description;

    private String location;

    private LocalDateTime requestDate;

    private List<Message> messages;


}
