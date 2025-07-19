package com.newdev.inservice.requestDtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class RoleDto {

    private String role;

    private int page = 0;

    private int size = 10;

}
