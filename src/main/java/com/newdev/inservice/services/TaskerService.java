package com.newdev.inservice.services;



import com.newdev.inservice.Mapping.UserMapper;
import com.newdev.inservice.exceptions.BadRequestException;
import com.newdev.inservice.exceptions.ResourceNotFoundException;
import com.newdev.inservice.models.*;

import com.newdev.inservice.models.enums.RoleEnum;
import com.newdev.inservice.models.enums.TaskerType;
import com.newdev.inservice.repository.UserRepository;

import com.newdev.inservice.requestDtos.PageDto;
import com.newdev.inservice.requestDtos.TaskerSearchDto;
import com.newdev.inservice.responseDtos.TaskerEntrepriseDto;
import com.newdev.inservice.responseDtos.TaskerIndividualDto;
import com.newdev.inservice.responseDtos.TaskerResponseDto;
import com.newdev.inservice.responseDtos.TaskerShopOwnerDto;
import com.newdev.inservice.serviceInterfaces.ITaskerService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TaskerService implements ITaskerService {


    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final MongoTemplate mongoTemplate;


    @Override
    public Object getTasker(String taskerId) {

        User user =  userRepository.findById(taskerId)
                .orElseThrow(() -> new ResourceNotFoundException("Tasker profile not found"));

        if(!user.getRole().equals(RoleEnum.TASKER))
            throw new ResourceNotFoundException("can only get a tasker");
        Tasker tasker = (Tasker) user;

        return switch (tasker.getTaskerType()) {
            case SHOP_OWNER -> userMapper.mapToTaskerShopOwnerDto(tasker);
            case ENTREPRISE -> userMapper.mapToTaskerEntrepriseDto(tasker);
            case INDIVIDUAL -> userMapper.mapToTaskerIndividualDto(tasker);
        };
    }

    @Override
    public Page<?> getAllTaskers(TaskerSearchDto filters) {

        Query query = new Query();

        // base condition: only taskers
        query.addCriteria(Criteria.where("role").is("TASKER"));

        // filter: skills (must contain at least one)
        if (filters.getSkills() != null && !filters.getSkills().isEmpty()) {
            query.addCriteria(Criteria.where("skill").in(filters.getSkills()));
        }

        // filter: min rating
        if (filters.getMinRating() != null) {
            query.addCriteria(Criteria.where("mainRating").gte(filters.getMinRating()));
        }

        // filter: type (INDIVIDUAL, SHOP_OWNER, ENTREPRISE)
        if (filters.getTaskerType() != null) {
            query.addCriteria(Criteria.where("taskerType").is(filters.getTaskerType()));
        }

        // filter: city
        if (filters.getCity() != null) {
            query.addCriteria(Criteria.where("taskerCity").is(filters.getCity()));
        }

        // count total
        long total = mongoTemplate.count(query, Tasker.class);

        Pageable pageable = PageRequest.of(filters.getPage(),filters.getSize());

        // apply pagination
        query.with(pageable);

        // execute query
        List<Tasker> taskers = mongoTemplate.find(query, Tasker.class);


        if (filters.getTaskerType() == null) {
            // Return All if no filter
            List<TaskerResponseDto> genericDtos = taskers.stream()
                    .map(userMapper::toTaskerResponse)
                    .toList();
            return new PageImpl<>(genericDtos, pageable, total);

        } else if (filters.getTaskerType() == TaskerType.SHOP_OWNER) {
            List<TaskerShopOwnerDto> shopOwnerDtos = taskers.stream()
                    .map(userMapper::mapToTaskerShopOwnerDto)
                    .toList();
            return new PageImpl<>(shopOwnerDtos, pageable, total);

        } else if (filters.getTaskerType() == TaskerType.ENTREPRISE) {
            List<TaskerEntrepriseDto> entrepriseDtos = taskers.stream()
                    .map(userMapper::mapToTaskerEntrepriseDto)
                    .toList();
            return new PageImpl<>(entrepriseDtos, pageable, total);
        }

        // fallback (INDIVIDUAL)
        List<TaskerIndividualDto> individualDtos = taskers.stream()
                .map(userMapper::mapToTaskerIndividualDto)
                .toList();
        return new PageImpl<>(individualDtos, pageable, total);

    }

}
