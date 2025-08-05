package com.newdev.inservice.responseDtos;

import com.newdev.inservice.models.enums.Gender;
import com.newdev.inservice.models.enums.RoleEnum;
import com.newdev.inservice.models.enums.SkillType;
import com.newdev.inservice.models.enums.TaskerType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record TaskerShopOwnerDto(String id,
                                 String fName,
                                 String lName,
                                 Gender gender,
                                 LocalDate birthdate,
                                 String cin,
                                 String phone,
                                 String email,
                                 RoleEnum role,
                                 String profileImage,
                                 LocalDateTime createdAt,
                                 LocalDateTime updatedAt,
                                 TaskerType taskerType,
                                 SkillType skill,
                                 String taskerCity,
                                 String taskerArea,
                                 String experience,
                                 int jobNumber,
                                 int rating,
                                 List<String> reviews,
                                 String mainPicture,
                                 List<String> pictures,
                                 boolean verified,
                                 String shopAddress,
                                 int shopLicenceNumber,
                                 List<DemandResponseDto> demands,
                                 List<JobResponseDto> jobs) {}
