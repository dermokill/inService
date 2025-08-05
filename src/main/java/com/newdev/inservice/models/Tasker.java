package com.newdev.inservice.models;

import lombok.*;

import com.newdev.inservice.models.enums.SkillType;
import com.newdev.inservice.models.enums.TaskerType;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter


public class Tasker extends User {

    private TaskerType taskerType;

    private SkillType skill;

    private String taskerCity;

    private String taskerArea;

    private String experience;

    private int jobNumber;

    private int rating;

    private List<String> reviews = new ArrayList<>();

    private String mainPicture;

    private List<String> pictures =  new ArrayList<>();

    private boolean verified = false;

    private ShopOwner shopOwner;

    private Enterprise enterprise;

    @DBRef
    private List<Demand> demands =  new ArrayList<>();

    @DBRef
    private List<Job> jobs =   new ArrayList<>();
}
