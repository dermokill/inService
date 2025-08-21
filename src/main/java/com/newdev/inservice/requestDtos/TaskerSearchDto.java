package com.newdev.inservice.requestDtos;


import com.newdev.inservice.models.enums.SkillType;
import com.newdev.inservice.models.enums.TaskerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class TaskerSearchDto {


    private List<SkillType> skills;         // e.g. ["PLOMBIER", "ELECTRICIEN"]

    private Double minRating;            // e.g. 4.0

    private TaskerType taskerType;       // "INDIVIDUAL", "SHOP_OWNER", "ENTREPRISE"

    private String city;                 // filter by city

    private int page = 0;

    private int size = 10;
}
