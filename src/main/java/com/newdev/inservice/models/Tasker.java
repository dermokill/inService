package com.newdev.inservice.models;

import lombok.*;

import com.newdev.inservice.models.enums.SkillType;
import com.newdev.inservice.models.enums.TaskerType;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter


public class Tasker extends User {

    private TaskerType taskerType;

    private SkillType skill;

    private String city;

    private String area;

    private String experience;

    private int jobNumber;

    private int rating;

    private List<String> reviews;

    private String mainPicture;

    private List<String> pictures;

    private boolean verified;

    private ShopOwner shopOwner;

    private Enterprise enterprise;

    @DBRef
    private List<Demand> demands;

    @DBRef
    private List<Job> jobs;
}
