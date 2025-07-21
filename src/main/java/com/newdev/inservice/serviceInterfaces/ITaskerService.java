package com.newdev.inservice.serviceInterfaces;


import com.newdev.inservice.models.User;
import com.newdev.inservice.requestDtos.DemandDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public interface ITaskerService {

     User getTasker(String id);

}
