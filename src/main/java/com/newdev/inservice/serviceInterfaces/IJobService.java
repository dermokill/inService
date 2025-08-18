package com.newdev.inservice.serviceInterfaces;


import com.newdev.inservice.requestDtos.ClientReviewDto;
import com.newdev.inservice.requestDtos.JobStatusDto;
import com.newdev.inservice.requestDtos.PageDto;
import com.newdev.inservice.responseDtos.JobResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public interface IJobService {

    Page<JobResponseDto> getAllJobs(UserDetails userDetails, PageDto pageDto);

    JobResponseDto getJobById(String jobId);

    void validateJob(UserDetails userDetails, String jobId, JobStatusDto jobStatusDto);

    void addReview(UserDetails userDetails,String jobId, ClientReviewDto clientReviewDto);
}
