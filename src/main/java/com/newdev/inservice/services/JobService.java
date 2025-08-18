package com.newdev.inservice.services;


import com.newdev.inservice.Mapping.UserMapper;
import com.newdev.inservice.exceptions.BadRequestException;
import com.newdev.inservice.exceptions.ConflictException;
import com.newdev.inservice.exceptions.ResourceNotFoundException;
import com.newdev.inservice.exceptions.UnauthorizedException;
import com.newdev.inservice.models.*;
import com.newdev.inservice.models.enums.JobStatus;
import com.newdev.inservice.models.enums.RoleEnum;
import com.newdev.inservice.repository.JobRepository;
import com.newdev.inservice.repository.UserRepository;
import com.newdev.inservice.requestDtos.ClientReviewDto;
import com.newdev.inservice.requestDtos.JobStatusDto;
import com.newdev.inservice.requestDtos.PageDto;
import com.newdev.inservice.responseDtos.JobResponseDto;
import com.newdev.inservice.serviceInterfaces.IJobService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class JobService implements IJobService {


    private final UserRepository userRepository;

    private final JobRepository jobRepository;

    private final UserMapper userMapper;

    @Override
    public Page<JobResponseDto> getAllJobs(UserDetails userDetails, PageDto pageDto) {

        User user2 = Optional.ofNullable(userDetails.getUsername())
                .map(userRepository::findByEmail)
                .orElseThrow(() -> new ResourceNotFoundException("user profile not found"));

        Pageable pageable = PageRequest.of(pageDto.getPage(), pageDto.getSize(),
                Sort.by("createdAt").ascending());

        Page<Job> jobPage;
        if (user2 instanceof Tasker tasker) {
            jobPage = jobRepository.findByTaskerId(tasker.getId(), pageable);
        }else if (user2 instanceof Client client) {
            jobPage = jobRepository.findByClientId(client.getId(), pageable);
        }else {
            throw new UnauthorizedException("Unsupported user type");
        }
        return jobPage.map(userMapper::mapToJobDto);
    }

    @Override
    public JobResponseDto getJobById(String jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job with id " + jobId + " not found"));

        return userMapper.mapToJobDto(job);
    }

    @Override
    public void validateJob(UserDetails userDetails, String jobId, JobStatusDto jobStatusDto) {

        User user = Optional.ofNullable(userDetails.getUsername())
                .map(userRepository::findByEmail)
                .orElseThrow(() -> new ResourceNotFoundException("user profile not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job with id " + jobId + " not found"));

        if (!(user instanceof Tasker) && !user.getId().equals(job.getTasker().getId()))
            throw new UnauthorizedException("only the authorised tasker can validate this job");

        if(!job.getStatus().equals(JobStatus.IN_PROGRESS))
            throw new UnauthorizedException("this job is already completed or canceled");

        JobStatus status = JobStatus.valueOf(jobStatusDto.getJobStatus().toString().toUpperCase());

        switch (status) {
            case COMPLETED -> {
                job.setStatus(JobStatus.COMPLETED);
                job.setFinishedAt(LocalDateTime.now());
                job.setUpdatedAt(LocalDateTime.now());
                jobRepository.save(job);
            }
            case CANCELED -> {
                job.setStatus(JobStatus.CANCELED);
                job.setFinishedAt(LocalDateTime.now());
                job.setUpdatedAt(LocalDateTime.now());
                jobRepository.save(job);
            }
            case IN_PROGRESS -> throw new BadRequestException("can only validate the job by FINISHED or CANCELED");
            default -> throw new UnauthorizedException("Unsupported job status");
        }
    }

    @Override
    public void addReview(UserDetails userDetails, String jobId, ClientReviewDto clientReviewDto) {

        User loggedIn = Optional.ofNullable(userDetails.getUsername())
                .map(userRepository::findByEmail)
                .orElseThrow(() -> new ResourceNotFoundException("user profile not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job with id " + jobId + " not found"));

        if (!loggedIn.getId().equals(job.getClient().getId()))
            throw new UnauthorizedException("only the authorised client can leave a review");

        if(job.getStatus().equals(JobStatus.IN_PROGRESS))
            throw new UnauthorizedException("cant review a job in-progress");

        if(job.getClientRating() != 0)
            throw new ConflictException("you can only leave a single review");

        job.setUpdatedAt(LocalDateTime.now());
        job.setClientRating(clientReviewDto.getClientRating());
        job.setClientFeedback(clientReviewDto.getClientFeedback());
        jobRepository.save(job);

        Tasker tasker = (Tasker) userRepository.findById(job.getTasker().getId())
                .orElseThrow(() -> new ResourceNotFoundException("tasker with id " + job.getTasker().getId() + " not found"));

        tasker.setUpdatedAt(LocalDateTime.now());
        tasker.setJobNumber(tasker.getJobNumber() + 1);
        tasker.getReviews().add(clientReviewDto.getClientFeedback() + " By : "+ loggedIn.getFName() + loggedIn.getLName());

        tasker.getRatings().add(clientReviewDto.getClientRating());
        double avgRating = tasker.getRatings().stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(5.0);
        tasker.setMainRating(avgRating);
        userRepository.save(tasker);
    }


}
