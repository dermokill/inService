package com.newdev.inservice.controller;


import com.newdev.inservice.requestDtos.ClientReviewDto;
import com.newdev.inservice.requestDtos.JobStatusDto;
import com.newdev.inservice.requestDtos.PageDto;
import com.newdev.inservice.responseDtos.PagedResponseDto;
import com.newdev.inservice.serviceInterfaces.IJobService;
import com.newdev.inservice.services.JobService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/jobs")
@AllArgsConstructor
@Validated
public class JobController {

    private final IJobService jobService;

    // get all jobs for the client or the tasker // returns paged list
    @GetMapping
    public ResponseEntity<?> getAllJobs(@AuthenticationPrincipal UserDetails userDetails,
                                        @RequestBody PageDto pageDto) {
        return ResponseEntity.ok(new PagedResponseDto<>(jobService.getAllJobs(userDetails, pageDto)));
    }

    // get job by id
    @GetMapping("/{jobId}")
    public ResponseEntity<?> getJobById(@PathVariable String jobId) {
        return new ResponseEntity<>(jobService.getJobById(jobId), HttpStatus.OK);
    }

    //Tasker Only
    // Validate the job ( COMPLETED , CANCELED )
    @PutMapping("{jobId}")
    public ResponseEntity<?> validateJob(@AuthenticationPrincipal UserDetails userDetails,
                                         @PathVariable String jobId,
                                         @RequestBody @Valid JobStatusDto jobStatusDto) {
        jobService.validateJob(userDetails, jobId, jobStatusDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //Client only
    // client gives a review in stars(from 1 to 5) and a comment
    @PutMapping("{jobId}/review")
    public ResponseEntity<?> addReview(@AuthenticationPrincipal UserDetails userDetails,
                                       @PathVariable String jobId,
                                       @RequestBody @Valid ClientReviewDto clientReviewDto){
        jobService.addReview(userDetails, jobId, clientReviewDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
