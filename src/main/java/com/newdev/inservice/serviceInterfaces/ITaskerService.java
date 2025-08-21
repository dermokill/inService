package com.newdev.inservice.serviceInterfaces;


import com.newdev.inservice.models.User;
import com.newdev.inservice.requestDtos.DemandDto;
import com.newdev.inservice.requestDtos.PageDto;
import com.newdev.inservice.requestDtos.TaskerSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public interface ITaskerService {

     Object getTasker(String taskerId);

     Page<?> getAllTaskers(TaskerSearchDto filters);



}
