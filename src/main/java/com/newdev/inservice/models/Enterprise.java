package com.newdev.inservice.models;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString

public class Enterprise {

    private String entrepriseAddress;

    private String entrepriseName;

    private int entrepriseLicenceNumber;

    private int employeeNumber;
}
