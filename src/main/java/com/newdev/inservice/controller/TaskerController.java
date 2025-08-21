package com.newdev.inservice.controller;


import com.newdev.inservice.requestDtos.DemandDto;
import com.newdev.inservice.requestDtos.TaskerSearchDto;
import com.newdev.inservice.responseDtos.PagedResponseDto;
import com.newdev.inservice.serviceInterfaces.IDemandService;
import com.newdev.inservice.serviceInterfaces.ITaskerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/taskers")
@Validated
public class TaskerController {


    private final ITaskerService taskerService;

    private final IDemandService demandService;

    @Autowired
    public TaskerController(ITaskerService taskerService,
                            IDemandService demandService) {
        this.taskerService = taskerService;
        this.demandService = demandService;
    }

    // Get all taskers by skillsType(plombier,mason...),
    // taskerType(INDIVIDUAL,SHOP_OWNER,ENTREPRISE),
    // taskerArea(location),
    // rating
    // returns paged list
    @GetMapping
    public ResponseEntity<?> getAllTaskers(@AuthenticationPrincipal UserDetails userDetails,
                                           @RequestBody TaskerSearchDto taskerSearchDto) {

        Page<?> taskers = taskerService.getAllTaskers(taskerSearchDto);
        return new ResponseEntity<>(new PagedResponseDto<>(taskers),HttpStatus.OK);
    }

    // get a single tasker by his id
    @GetMapping("{taskerId}")
    public ResponseEntity<?> getTasker(@PathVariable String taskerId) {
        return ResponseEntity.ok(taskerService.getTasker(taskerId));
    }

    //Client only
    // add a new demand, needs to select the tasker first then click add new demand to him
    @PostMapping("{taskerId}/demands")
    public ResponseEntity<?> newDemand (@AuthenticationPrincipal UserDetails userDetails,
                                        @PathVariable String taskerId,
                                        @RequestBody @Valid DemandDto demandDto)
    {
        demandService.addDemand(userDetails,demandDto,taskerId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }





}
