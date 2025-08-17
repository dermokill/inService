package com.newdev.inservice.serviceInterfaces;


import com.newdev.inservice.requestDtos.DemandDto;
import com.newdev.inservice.requestDtos.MessageRequestDto;
import com.newdev.inservice.requestDtos.PageDto;
import com.newdev.inservice.responseDtos.DemandResponseDto;
import com.newdev.inservice.responseDtos.JobResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public interface IDemandService {

    void addDemand (UserDetails userDetails, DemandDto demandDto, String taskerId);

    DemandResponseDto getDemandById (String demandId);

    Page<DemandResponseDto> getAllDemands (UserDetails userDetails, PageDto  pageDto);

    void newMessage(UserDetails userDetails, String demandId, MessageRequestDto  messageRequestDto);

    void demandRefused(UserDetails userDetails, String demandId);

    JobResponseDto demandAccepted(UserDetails userDetails, String demandId);

}
