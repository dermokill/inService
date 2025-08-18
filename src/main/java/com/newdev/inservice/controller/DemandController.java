package com.newdev.inservice.controller;


import com.newdev.inservice.exceptions.BadRequestException;
import com.newdev.inservice.exceptions.UnauthorizedException;
import com.newdev.inservice.models.enums.DemandStatus;
import com.newdev.inservice.requestDtos.DemandStatusDto;
import com.newdev.inservice.requestDtos.MessageRequestDto;
import com.newdev.inservice.requestDtos.PageDto;
import com.newdev.inservice.responseDtos.JobResponseDto;
import com.newdev.inservice.responseDtos.JsonResponse;
import com.newdev.inservice.responseDtos.PagedResponseDto;
import com.newdev.inservice.serviceInterfaces.IDemandService;
import com.newdev.inservice.services.DemandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/demands")
@RequiredArgsConstructor
@Validated
public class DemandController {


    private final IDemandService demandService;

    // get all demands for the client or the tasker // returns paged list
    @GetMapping
    public ResponseEntity<?> getAllDemands(@AuthenticationPrincipal UserDetails userDetails,
                                           @RequestBody PageDto PageDto) {
        return ResponseEntity.ok(new PagedResponseDto<>(demandService.getAllDemands(userDetails,PageDto)));
    }

    // send message in a specific demand
    @PutMapping("{demandId}/messages")
    public ResponseEntity<?> newMessage(@AuthenticationPrincipal UserDetails userDetails,
                                        @PathVariable String demandId,
                                        @RequestBody MessageRequestDto messageRequestDto) {

        demandService.newMessage(userDetails, demandId, messageRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // get demand by id
    @GetMapping("/{demandId}")
    public ResponseEntity<?> getDemandById(@PathVariable String demandId) {
        return new ResponseEntity<>(demandService.getDemandById(demandId),HttpStatus.OK);
    }

    // TASKER ONLY
    // frontend will send : ACCEPTED , REFUSED
    @PutMapping("/{demandId}")
    public ResponseEntity<?> validateDemand(@AuthenticationPrincipal UserDetails userDetails,
                                                  @PathVariable String demandId,
                                                  @RequestBody @Valid DemandStatusDto demandStatusDto) {

        // lkhedma lm3awda hhhh
        DemandStatus status = DemandStatus.valueOf(demandStatusDto.getDemandStatus().toString().toUpperCase());

        return switch (status) {
            case REFUSED -> {
                demandService.demandRefused(userDetails, demandId);
                yield new ResponseEntity<>(HttpStatus.OK);
            }
            case ACCEPTED -> {
                JobResponseDto jobResponseDto = demandService.demandAccepted(userDetails, demandId);
                yield ResponseEntity.ok(jobResponseDto);
            }
            case  PENDING -> throw new BadRequestException("can only validate the demand by ACCEPTED or REFUSED");
        };
    }


}
