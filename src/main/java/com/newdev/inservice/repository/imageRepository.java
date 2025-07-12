package com.newdev.inservice.repository;


import com.newdev.inservice.models.ImageTest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface imageRepository extends MongoRepository<ImageTest, String> {
}
