package com.newdev.inservice.services;


import com.newdev.inservice.exceptions.BadRequestException;
import com.newdev.inservice.exceptions.ResourceNotFoundException;
import com.newdev.inservice.exceptions.UnauthorizedException;
import com.newdev.inservice.models.*;
import com.newdev.inservice.models.enums.DemandStatus;
import com.newdev.inservice.models.enums.RoleEnum;
import com.newdev.inservice.repository.DemandRepository;
import com.newdev.inservice.repository.UserRepository;
import com.newdev.inservice.requestDtos.DemandDto;
import com.newdev.inservice.requestDtos.PageDto;
import com.newdev.inservice.serviceInterfaces.IDemandService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class DemandService implements IDemandService {

    private final DemandRepository demandRepository;

    private final UserRepository userRepository;

    public DemandService(DemandRepository demandRepository,
                         UserRepository userRepository) {
        this.demandRepository = demandRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void addDemand(UserDetails userDetails, DemandDto demandDto, String taskerId) {

        User user = Optional.ofNullable(userDetails.getUsername())
                .map(userRepository::findByEmail)
                .orElseThrow(() -> new ResourceNotFoundException("client profile not found"));

        if(!user.getRole().equals(RoleEnum.CLIENT))
            throw new UnauthorizedException("only Clients can request a demand from a tasker");
        Client client = (Client) user;

        User user2 = userRepository.findById(taskerId)
                .orElseThrow(() -> new ResourceNotFoundException("Tasker profile not found"));

        if(!user2.getRole().equals(RoleEnum.TASKER))
            throw new UnauthorizedException("only Taskers can receive demand from a client");
        Tasker tasker = (Tasker) user2;

        List<Message> messages = new ArrayList<>();
        List<Demand> demands = new ArrayList<>();

        // Format : 2024-07-21T18:30
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime dateTime;

        try {
            dateTime = LocalDateTime.parse(demandDto.getRequestDate(), formatter);
        } catch (DateTimeParseException e) {
            System.out.println(e.getMessage());
            throw new BadRequestException("Invalid Request date format. Please use this format 2024-07-21T18:30");
        }


        Message message = new Message();
        message.setSender(client);
        message.setContent(demandDto.getMessage());
        messages.add(message);

        Demand demand = new Demand();
        demand.setTasker(tasker);
        demand.setClient(client);
        demand.setDescription(demandDto.getDescription());
        demand.setLocation(demandDto.getLocation());
        demand.setTaskType(tasker.getSkill());
        demand.setStatus(DemandStatus.PENDING);
        demand.setRequestDate(dateTime);
        demand.setMessages(messages);

        demandRepository.save(demand);
        //messageRepository.save(message);

       /* demands.add(demand);
        tasker.setDemands(demands);
        client.setDemands(demands);
        userRepository.save(tasker);
        userRepository.save(client); */
    }

    @Override
    public Demand getDemandById(String demandId) {
        return null;
    }

    @Override
    public Page<Demand> getDemandsByTasker(UserDetails userDetails, PageDto pageDto) {

        User user2 = Optional.ofNullable(userDetails.getUsername())
                .map(userRepository::findByEmail)
                .orElseThrow(() -> new ResourceNotFoundException("tasker profile not found"));

        if(!user2.getRole().equals(RoleEnum.TASKER))
            throw new UnauthorizedException("only Taskers can use this api");
        Tasker tasker = (Tasker) user2;

        Pageable pageable = PageRequest.of(pageDto.getPage(), pageDto.getSize(),
                Sort.by("createdAt").ascending());

        Page<Demand> demands = demandRepository.findByTaskerId(tasker.getId(), pageable);

        if(!tasker.getId().equals(demands.getContent().get(0).getTasker().getId()))
            throw new UnauthorizedException("Only Taskers can see their Demands");

        for (Demand demand : demands.getContent()) {
            if (demand.getTasker() != null)
                demand.getTasker().setPassword("");

            if (demand.getClient() != null)
                demand.getClient().setPassword("");

            if (demand.getMessages() != null) {
                demand.getMessages().forEach(message -> {
                    if (message.getSender() != null) {
                        message.getSender().setPassword("");
                    }
                });
            }
        }
        return demands;
    }


}
