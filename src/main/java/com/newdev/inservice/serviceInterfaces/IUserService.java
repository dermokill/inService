package com.newdev.inservice.serviceInterfaces;


import com.newdev.inservice.models.User;
import com.newdev.inservice.requestDtos.ImagesDto;
import com.newdev.inservice.requestDtos.RegisterClientDto;
import com.newdev.inservice.requestDtos.RegisterTaskerDto;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IUserService {

    User getProfile(UserDetails userDetails);

    Page<User> getClientsAndAdmins(UserDetails userDetails, String role, int page, int size);

    String insertClient(RegisterClientDto dto);

    String insertTasker(RegisterTaskerDto dto) throws Exception;

    void insertTaskerImages (UserDetails userDetails, ImagesDto imagesDto) throws Exception;

    void validateTasker (UserDetails userDetails, String id);

}
