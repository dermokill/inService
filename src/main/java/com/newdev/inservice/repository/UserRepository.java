package com.newdev.inservice.repository;

import com.newdev.inservice.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface UserRepository  extends MongoRepository<User, UUID> {
}
