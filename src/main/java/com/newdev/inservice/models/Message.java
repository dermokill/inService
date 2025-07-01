package com.newdev.inservice.models;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString

public class Message {

    private String id =  UUID.randomUUID().toString();

    @DBRef
    private User sender;

    private String content;
    private LocalDateTime sentAt;
}
