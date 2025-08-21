package com.newdev.inservice.responseDtos;

import com.newdev.inservice.models.enums.Gender;
import com.newdev.inservice.models.enums.SkillType;
import com.newdev.inservice.models.enums.TaskerType;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class TaskerResponseDto {
    private String id;
    private String firstName;
    private String lastName;
    private Gender gender;
    private LocalDate birthdate;
    private String cin;
    private String phone;
    private String email;

    // Tasker-specific common fields
    private TaskerType taskerType;   // INDIVIDUAL, SHOP_OWNER, ENTREPRISE
    private SkillType skill;
    private String taskerCity;
    private String taskerArea;
    private String experience;
    private Integer jobNumber;
    private Double rating;

    // ShopOwner specific
    private String shopAddress;
    private Integer shopLicenceNumber;

    // Entreprise specific
    private String entrepriseAddress;
    private String entrepriseName;
    private Integer entrepriseLicenceNumber;
    private Integer employeeNumber;

    private List<DemandResponseDto> demands;

    private List<JobResponseDto> jobs;
}
