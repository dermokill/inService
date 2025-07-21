package com.newdev.inservice.serviceInterfaces;


import com.newdev.inservice.models.Demand;
import com.newdev.inservice.requestDtos.DemandDto;
import com.newdev.inservice.requestDtos.PageDto;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IDemandService {

    void addDemand (UserDetails userDetails, DemandDto demandDto, String taskerId);

    Demand getDemandById (String demandId);

    Page<Demand> getDemandsByTasker (UserDetails userDetails, PageDto  pageDto);

}
