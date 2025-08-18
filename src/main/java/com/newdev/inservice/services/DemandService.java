package com.newdev.inservice.services;


import com.newdev.inservice.Mapping.UserMapper;
import com.newdev.inservice.exceptions.BadRequestException;
import com.newdev.inservice.exceptions.ConflictException;
import com.newdev.inservice.exceptions.ResourceNotFoundException;
import com.newdev.inservice.exceptions.UnauthorizedException;
import com.newdev.inservice.models.*;
import com.newdev.inservice.models.enums.DemandStatus;
import com.newdev.inservice.models.enums.RoleEnum;
import com.newdev.inservice.repository.DemandRepository;
import com.newdev.inservice.repository.JobRepository;
import com.newdev.inservice.repository.UserRepository;
import com.newdev.inservice.requestDtos.DemandDto;
import com.newdev.inservice.requestDtos.MessageRequestDto;
import com.newdev.inservice.requestDtos.PageDto;
import com.newdev.inservice.responseDtos.DemandResponseDto;
import com.newdev.inservice.responseDtos.JobResponseDto;
import com.newdev.inservice.serviceInterfaces.IDemandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
public class DemandService implements IDemandService {

    private final DemandRepository demandRepository;

    private final UserRepository userRepository;

    private final EmailService emailService;

    private final WhatsAppService whatsAppService;

    private final UserMapper userMapper;

    private final JobRepository jobRepository;

    @Autowired
    public DemandService(DemandRepository demandRepository,
                         UserRepository userRepository,
                         EmailService emailService,
                         WhatsAppService whatsAppService,
                         UserMapper userMapper, JobRepository jobRepository) {
        this.demandRepository = demandRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.whatsAppService = whatsAppService;
        this.userMapper = userMapper;
        this.jobRepository = jobRepository;
    }

    @Override
    public void addDemand(UserDetails userDetails, DemandDto demandDto, String taskerId) {

        User user = Optional.ofNullable(userDetails.getUsername())
                .map(userRepository::findByEmail)
                .orElseThrow(() -> new ResourceNotFoundException("client profile not found"));

        if (user.getId().equals(taskerId))
            throw new UnauthorizedException("You cant request a task from yourself");

        // Any User can request a task no matter the role
        Client client = (Client) user; // FIX TASKER CANT BE CAST TO CLIENT LATER

        User user2 = userRepository.findById(taskerId)
                .orElseThrow(() -> new ResourceNotFoundException("Tasker profile not found"));

        if (!user2.getRole().equals(RoleEnum.TASKER))
            throw new UnauthorizedException("only Taskers can receive demand from a client");
        Tasker tasker = (Tasker) user2;


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

        Demand demand = new Demand();
        demand.setTasker(tasker);
        demand.setClient(client);
        demand.setDescription(demandDto.getDescription());
        demand.setLocation(demandDto.getLocation());
        demand.setTaskType(tasker.getSkill());
        demand.setStatus(DemandStatus.PENDING);
        demand.setRequestDate(dateTime);
        demand.setMessages(List.of(message));

        demandRepository.save(demand);

        tasker.getDemands().add(demand);
        client.getDemands().add(demand);
        userRepository.save(tasker);
        userRepository.save(client);

        //Sending the email to tasker
        String subject = "New Demand Received";
        String body = String.format("Hello %s,\n\nYou have received a new demand from %s %s.\n\nDescription: %s\n\nLocation: %s\n\nRequest Date: %s. \n\nClient-Message: %s. \n\nPlease log in to your dashboard to respond.",
                tasker.getFName(),
                client.getFName(),
                client.getLName(),
                demand.getDescription(),
                demand.getLocation(),
                demand.getRequestDate().toString().substring(0,10)+" at "+demand.getRequestDate().toString().substring(11),
                message.getContent());
        emailService.sendEmail(tasker.getEmail(), subject, body);

        //Sending whatsapp message to tasker
        String whatsappMessage = String.format(
                "Hi %s 👋,\nYou just received a new demand from %s %s.\n\n📌 Description: %s\n\n📍 Location: %s\n\n📅 Date: %s. \n\nClient-Message: %s. \n\nPlease check your dashboard.",
                tasker.getFName(),
                client.getFName(),
                client.getLName(),
                demand.getDescription(),
                demand.getLocation(),
                demand.getRequestDate().toString().substring(0,10)+" at "+demand.getRequestDate().toString().substring(11),
                message.getContent()
        );
        whatsAppService.sendWhatsAppMessage(tasker.getPhone(), whatsappMessage);
    }

    @Override
    public DemandResponseDto getDemandById(String demandId) {

        Demand demand = demandRepository.findById(demandId)
                .orElseThrow(() -> new ResourceNotFoundException("Demand with id " + demandId + " not found"));

        return userMapper.mapToDemandDto(demand);
    }

    @Override
    public Page<DemandResponseDto> getAllDemands(UserDetails userDetails, PageDto pageDto) {

        User user2 = Optional.ofNullable(userDetails.getUsername())
                .map(userRepository::findByEmail)
                .orElseThrow(() -> new ResourceNotFoundException("user profile not found"));

        Pageable pageable = PageRequest.of(pageDto.getPage(), pageDto.getSize(),
                Sort.by("createdAt").ascending());

        Page<Demand> demandPage;

        if (user2 instanceof Tasker tasker) {
            demandPage = demandRepository.findByTaskerId(tasker.getId(), pageable);
        }else if (user2 instanceof Client client) {
            demandPage = demandRepository.findByClientId(client.getId(), pageable);
        }else {
            throw new UnauthorizedException("Unsupported user type");
        }
        return demandPage.map(userMapper::mapToDemandDto);
    }

    @Override
    public void newMessage(UserDetails userDetails, String demandId, MessageRequestDto messageRequestDto) {

        User loggedUser = Optional.ofNullable(userDetails.getUsername())
                .map(userRepository::findByEmail)
                .orElseThrow(() -> new ResourceNotFoundException("user profile not found"));

        Demand demand = demandRepository.findById(demandId)
                .orElseThrow(() -> new ResourceNotFoundException("Demand with id " + demandId + " not found"));

        boolean isTasker = loggedUser.getId().equals(demand.getTasker().getId());
        boolean isClient = loggedUser.getId().equals(demand.getClient().getId());

        if (!isTasker && !isClient) {
            throw new UnauthorizedException("Unauthorized to send message in this demand");
        }

        Message message = new Message();
        message.setSender(loggedUser);
        message.setContent(messageRequestDto.getMessage());
        message.setSentAt(LocalDateTime.now());

        demand.getMessages().add(message);
        demand.setUpdatedAt(LocalDateTime.now());
        demandRepository.save(demand);
    }

    @Override
    public void demandRefused(UserDetails userDetails, String demandId) {

        User user = Optional.ofNullable(userDetails.getUsername())
                .map(userRepository::findByEmail)
                .orElseThrow(() -> new ResourceNotFoundException("tasker profile not found"));

        Demand demand = demandRepository.findById(demandId)
                .orElseThrow(() -> new ResourceNotFoundException("Demand with id " + demandId + " not found"));

        if(!(user instanceof Tasker) && !user.getId().equals(demand.getTasker().getId()))
            throw new UnauthorizedException("only the authorised tasker can validate this demand");

        if(demand.getStatus().equals(DemandStatus.REFUSED))
            throw new ConflictException("Demand is already refused");

        demand.setStatus(DemandStatus.REFUSED);
        demand.setUpdatedAt(LocalDateTime.now());
        demandRepository.save(demand);
    }

    @Override
    public JobResponseDto demandAccepted(UserDetails userDetails, String demandId) {

        User user = Optional.ofNullable(userDetails.getUsername())
                .map(userRepository::findByEmail)
                .orElseThrow(() -> new ResourceNotFoundException("tasker profile not found"));

        Demand demand = demandRepository.findById(demandId)
                .orElseThrow(() -> new ResourceNotFoundException("Demand with id " + demandId + " not found"));

        if(!user.getRole().equals(RoleEnum.TASKER) && !user.getId().equals(demand.getTasker().getId()))
            throw new UnauthorizedException("only the authorised tasker can validate this demand");

        if(demand.getStatus().equals(DemandStatus.ACCEPTED))
            throw new ConflictException("Demand is already accepted");
        Tasker tasker = (Tasker) user;


        demand.setStatus(DemandStatus.ACCEPTED);
        demand.setUpdatedAt(LocalDateTime.now());
        demandRepository.save(demand);

        Job job = new Job();
        job.setOriginalDemand(demand);
        job.setClient(demand.getClient());
        job.setTasker(tasker);
        jobRepository.save(job);

        tasker.getJobs().add(job);
        userRepository.save(tasker);

        Client client = (Client) userRepository.findById(demand.getClient().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Client with id " + demand.getClient().getId() + " not found"));
        client.getJobs().add(job);
        userRepository.save(client);

        return userMapper.mapToJobDto(job);
    }

}
