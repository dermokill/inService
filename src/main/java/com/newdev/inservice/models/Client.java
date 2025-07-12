package com.newdev.inservice.models;


import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter


public class Client extends User {

    private String clientCity;

    private String clientArea;

    private String personalAddress;

    @DBRef
    private List<Demand> demands;

    @DBRef
    private List<Job> jobs;

}


