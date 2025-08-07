package com.newdev.inservice.serviceInterfaces;


import com.newdev.inservice.requestDtos.DemandDto;
import com.newdev.inservice.requestDtos.PageDto;
import com.newdev.inservice.responseDtos.DemandResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public interface IDemandService {

    void addDemand (UserDetails userDetails, DemandDto demandDto, String taskerId);

    DemandResponseDto getDemandById (String demandId);

    Page<DemandResponseDto> getDemandsByTasker (UserDetails userDetails, PageDto  pageDto);

}
