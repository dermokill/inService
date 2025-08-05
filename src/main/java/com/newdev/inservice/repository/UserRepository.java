package com.newdev.inservice.repository;

import com.newdev.inservice.models.Admin;
import com.newdev.inservice.models.Client;
import com.newdev.inservice.models.User;
import com.newdev.inservice.models.enums.RoleEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface UserRepository  extends MongoRepository<User, String> {

    User findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByIdIsNotAndEmail(String id, String email);

    Page<User> findByRole(RoleEnum role, Pageable pageable);

    User getById(String uuid);
}
