package com.newdev.inservice.responseDtos;

import com.newdev.inservice.models.enums.RoleEnum;
import com.newdev.inservice.models.enums.SkillType;
import com.newdev.inservice.models.enums.TaskerType;

public record SubTaskerDto(String taskerId,
                           String taskerName,
                           RoleEnum taskerRole,
                           TaskerType taskerType,
                           SkillType skillType) {
}
